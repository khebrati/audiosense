This document addresses some commonly occurring anti-patterns when developing apps with jetpack compose , compose multiplatform technologies with MVVM architecture. Each section contains an example of the antipattern in question, and provides a refactoring recipe for it. 

# [State hoisting](https://developer.android.com/develop/ui/compose/documentation)

Are all states in view model? Are all of them in UI? when to choose which layer to be the owner of the data?

## Hoist in UI

When ever the state is controlling a UI related behavior (Layout change, color change, animation, font change), then it makes sense to just hoist the state in UI and leave the view model unaware. Example:

#Beautiful
```kotlin
@Composable  
fun PodcastDetailsDescription(podcast: PodcastInfo, modifier: Modifier) {  
    var isExpanded by remember { mutableStateOf(false) }  
    var showSeeMore by remember { mutableStateOf(false) }  
    Box(  
        modifier = modifier.clickable { isExpanded = !isExpanded },  
    ) {  
        Text(  
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,  
            //Other text parameters
        )  
        if (showSeeMore) {  
            Box{
	            //Content
            }  
        }  
    }  
}
```

In this example, since isExpanded and showSeeMore are controlling UI behavior (Expansion of a Box, a change in layout) , it makes perfect sense to hoist them in UI. 

## Hoist in view model

If you want to display "Information " in UI, and your state is not UI related, then you can hoist it in view model. Examples of this:

- Domain layer models
- Anything stored in your data layer
- Anything meaningful user input information (text field, ...)
- Any meaningful data calculated in domain layer and shown to user 

## UI logic vs business logic

- **Business logic** is the implementation of product requirements for app data. For example, bookmarking an article in a news reader app when the user taps the button. This logic to save a bookmark to a file or database is usually placed in the domain or data layers. The state holder usually delegates this logic to those layers by calling the methods they expose.
- **UI logic** is related to _how_ to display UI state on the screen. For example, obtaining the right search bar hint when the user has selected a category, scrolling to a particular item in a list, or the navigation logic to a particular screen when the user clicks a button.

# The forever State nightmare

The [Official docs](https://developer.android.com/develop/ui/compose/documentation) of jetpack compose suggest that you never use wrapper classes for arguments passed to your Composables, as they might reduce the visibility of their responsibilities. However, in Real world, if You keep following this pattern, soon you will end up with these functions like this:

#Ugly
```kotlin
 AddClientDialog(  
        openAddClientDialog,  
        onClickNewClient = { client -> clientScreenModel.addClient(client) },  
        clientFirstName = clientFirstName.value,  
        clientLastName = clientLastName.value,  
        clientMobile = clientMobile.value,  
        clientAddress = clientAddress.value,  
        clientCity = clientCity.value,  
        clientNationalCode = clientNationalCode.value,  
        clientPhysician = clientPhysician.value,  
        clientGender = clientGender.value,  
        clientBirthday = clientBirthday.value,  
        onClientFirstNameChange = { clientScreenModel.setClientFirstName(it) },  
        onClientLastNameChange = { clientScreenModel.setClientLastName(it) },  
        onClientMobileChange = { clientScreenModel.setClientMobile(it) },  
        onClientAddressChange = { clientScreenModel.setClientAddress(it) },  
        onClientCityChange = { clientScreenModel.setClientCity(it) },  
        onClientNationalCodeChange = { clientScreenModel.setClientNationalCode(it) },  
        onClientPhysicianChange = { clientScreenModel.setClientPhysician(it) },  
        onClientGenderChange = { clientScreenModel.setClientGender(it) },  
        onClientBirthdayChange = { clientScreenModel.setClientBirthday(it) },  
        disCardClient = { clientScreenModel.reset() },  
        onDismiss = { clientScreenModel.setOpenAddClientDialog(false) })  
}
```


This will even trigger static analyzers like [Detekt](https://detekt.dev/), and they will warn you that your function has too many input arguments.  

It is recommended that you do this instead:
1. For Each screen , have exactly one screen model, and one "State" class (which holds together all your states in one wrapper class ), and another "Action" interface (which wraps all UI callbacks)
2. mark that class as @Immutable
3. put every state that your screen needs inside this class. 

One such example:

## Handling states

#Beautiful 
```kotlin
class HomeViewModel(){

// Define all states as MutableStateFlow (all private)
private val selectedLibraryPodcast = MutableStateFlow<PodcastInfo?>(null)  
private val selectedHomeCategory = MutableStateFlow(HomeCategory.Discover)  
private val homeCategories = MutableStateFlow(HomeCategory.entries)  
private val _selectedCategory = MutableStateFlow<CategoryInfo?>(null)  
//Rest of them

//Have a central state class (only field that is exposed to UI)
private val _state = MutableStateFlow(HomeScreenUiState())
	val state: StateFlow<HomeScreenUiState>  
    get() = _state  
  
init {  
    viewModelScope.launch {         
        combine(  
            homeCategories,  
            selectedHomeCategory,  
            //Rest of MutableState flows
        ) {  
                homeCategories,  
                homeCategory,    
            ->  
            //Using combine, this will run anytime any of your 
		    //Flows emit (any change in any state). This way, anytime a state changes,
            //you can use this change to update your other states that rely on the 
            //one that's changed.
  
        }.catch { throwable ->  
            emit(  
                HomeScreenUiState(  
                    isLoading = false,  
                    errorMessage = throwable.message,  
                ),  
            )  
        }.collect {  
            _state.value = it  
        }  
    }  
}

}

@Immutable  
data class HomeScreenUiState(  
    val isLoading: Boolean = true,  
    val errorMessage: String? = null,  
    val selectedHomeCategory: HomeCategory = HomeCategory.Discover,  
    val homeCategories: List<HomeCategory> = emptyList(),  
    //Any other domain state needed in UI 
)
```

and then in your UI:

#Beautiful 
```kotlin
@Composable  
fun MainScreen(indowSizeClass: WindowSizeClass viewModel: HomeViewModel = inject()) {  
    val homeScreenUiState by viewModel.state.collectAsStateWithLifecycle() 
    //Pass this state to your other composables.
    MainScreenContent(
	    state = homeScreenUiState
    )
}
```

As a rule of thumb, if your Composable is so Root that it contains more than 8 arguments, then you should consider passing a UiState class. But if it is a simple dialog , then just pass in the required states:

#Beautiful 
```kotlin
val viewState by viewModel.state.collectAsStateWithLifeCycle()
ClientInfo(
	name = viewState.name,
	lastName = viewState.lastName
)
```

There is also a common pattern to define all these for a singular state:
- MutableStateFlow
- StateFlow
- Setter getter functions

like here:

#Ugly 
```kotlin

private val _clientFirstName = MutableStateFlow("")  
val clientFirstName = _clientFirstName.asStateFlow()  
fun setClientFirstName(name: String) {  
    _clientFirstName.value = name  
}  

val _clientLastName = MutableStateFlow("")  
val clientLastName = _clientLastName.asStateFlow()  
  
fun setClientLastName(name: String) {  
    _clientLastName.value = name  
}  
  
private val _clientMobile = MutableStateFlow("")  
val clientMobile = _clientMobile.asStateFlow()  
  
fun setClientMobile(mobile: String) {  
    _clientMobile.value = mobile  
}  
  
private val _openAddClientDialog = MutableStateFlow(false)  
val openAddClientDialog = _openAddClientDialog.asStateFlow()  
  
fun setOpenAddClientDialog(value: Boolean) {  
    _openAddClientDialog.value = value  
}  
  
private val _clientAddress = MutableStateFlow("")  
val clientAddress = _clientAddress.asStateFlow()  
  
fun setClientAddress(address: String) {  
    _clientAddress.value = address  
}  
  
private val _clientCity = MutableStateFlow("")  
val clientCity = _clientCity.asStateFlow()  
  
fun setClientCity(city: String) {  
    _clientCity.value = city  
}  
  
private val _clientNationalCode = MutableStateFlow("")  
val clientNationalCode = _clientNationalCode.asStateFlow()  
  
fun setClientNationalCode(nationalCode: String) {  
    _clientNationalCode.value = nationalCode  
}  
  
private val _clientPhysician = MutableStateFlow("")  
val clientPhysician = _clientPhysician.asStateFlow()  
  
fun setClientPhysician(physician: String) {  
    _clientPhysician.value = physician  
}  
  
private val _clientGender = MutableStateFlow("Male")  
val clientGender = _clientGender.asStateFlow()  
  
fun setClientGender(gender: String) {  
    _clientGender.value = gender  
}  
  
private val _clientBirthday =  
    MutableStateFlow(Birthday())  
val clientBirthday = _clientBirthday.asStateFlow()  
  
fun setClientBirthday(birthday: Birthday) {  
    _clientBirthday.value = birthday  
}  

```

Do this if you want you view model to reach 1000 lines quickly !

Instead, follow the pattern described above (having only one central state)

## Handling actions

Since a big portion of that lambda described above contains action callback received from UI, you can do this instead:

```kotlin
class ViewModel{
	//States
	fun onHomeAction(action: HomeAction) {  
	    when (action) {  
	        is HomeAction.CategorySelected -> onCategorySelected(action.category)  
	        is HomeAction.HomeCategorySelected ->  
	        onHomeCategorySelected(action.category)   
	        is HomeAction.PodcastUnfollowed -> onPodcastUnfollowed(action.podcast)  
	        //Other callbacks 
	    }  
	}
	fun onCategorySelected(category: Category){
		//TODO
	}
	fun onHomeCategorySelected(category: Category){
		//TODO
	}
	fun onPodcastUnfollowed(podcast: Podcast){
		//TODO
	}
}
@Immutable  
sealed interface HomeAction {  
    data class CategorySelected(val category: CategoryInfo) : HomeAction  
    data class HomeCategorySelected(val category: HomeCategory) : HomeAction  
    data class PodcastUnfollowed(val podcast: PodcastInfo) : HomeAction  
	//Other callbacks
}
```

In UI:
```kotlin
@Composable  
fun MainScreen(indowSizeClass: WindowSizeClass viewModel: HomeViewModel = inject()) {  
    val homeScreenUiState by viewModel.state.collectAsStateWithLifecycle() 
    //Pass this state to your other composables.
    MainScreenContent(
	    state = homeScreenUiState
	    action = viewModel::onHomeAction
    )
}
```

# Navigation

