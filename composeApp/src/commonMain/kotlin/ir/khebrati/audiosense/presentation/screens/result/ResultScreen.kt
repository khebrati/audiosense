@file:OptIn(ExperimentalMaterial3Api::class)

package ir.khebrati.audiosense.presentation.screens.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Textsms
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.khebrati.audiosense.presentation.components.AudiosenseAppBar
import ir.khebrati.audiosense.presentation.components.AudiosenseScaffold
import ir.khebrati.audiosense.presentation.components.LoadingScreen
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.DescriptiveResultRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.HomeRoute
import ir.khebrati.audiosense.presentation.navigation.AudiosenseRoute.ResultRoute
import ir.khebrati.audiosense.presentation.screens.result.TestResultIntent.*
import ir.khebrati.audiosense.presentation.screens.result.TestResultUiState.*
import ir.khebrati.audiosense.presentation.screens.result.components.Audiogram
import ir.khebrati.audiosense.presentation.screens.result.components.DoneButton
import ir.khebrati.audiosense.presentation.screens.result.components.HearingLossCard
import ir.khebrati.audiosense.presentation.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinNavViewModel

@Composable
fun ResultScreen(
    resultRoute: ResultRoute,
    onNavigateHome: (HomeRoute) -> Unit,
    onNavigateDescriptiveResult: (DescriptiveResultRoute) -> Unit,
    viewModel: TestResultViewModel = koinNavViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val graphicsLayer = rememberGraphicsLayer()
    val scope = rememberCoroutineScope()
    AudiosenseScaffold(
        contentPadding = PaddingValues(),
        topBar = { AppBarWithShare(resultRoute, viewModel, scope, graphicsLayer, onNavigateHome) },
    ) {
        if (uiState is Ready) {
            ReadyResultScreenContent(
                onNavigateHome,
                onNavigateDescriptiveResult,
                uiState,
                graphicsLayer,
            )
        } else LoadingScreen()
    }
}

@Composable
private fun AppBarWithShare(
    resultRoute: ResultRoute,
    viewModel: TestResultViewModel,
    scope: CoroutineScope,
    graphicsLayer: GraphicsLayer,
    onNavigateHome: (HomeRoute) -> Unit,
) {
    AudiosenseAppBar(
        title = resultRoute.title,
        canNavigateBack = true,
        actions = {
            ShareIcon(
                onShareText = { viewModel.handleIntent(ShareText) },
                onShareImage = {
                    scope.launch {
                        val bitmap = graphicsLayer.toImageBitmap()
                        viewModel.handleIntent(ShareImage(bitmap))
                    }
                },
            )
        },
        onNavigateBack = { onNavigateHome(HomeRoute) },
    )
}

@Composable
fun ShareIcon(onShareText: () -> Unit, onShareImage: () -> Unit) {
    var showBottomSheet by remember { mutableStateOf(false) }
    IconButton(onClick = { showBottomSheet = true }, modifier = Modifier.size(50.dp)) {
        Icon(imageVector = Icons.Default.Share, contentDescription = "Share test results")
    }
    if (showBottomSheet) {
        val padding = 15.dp
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            TitleRow(modifier = Modifier.padding(padding))
            BottomSheetRow(
                padding = padding,
                text = "Text",
                onClick = {
                    onShareText()
                    showBottomSheet = false
                },
                icon = Icons.Outlined.Textsms,
            )
            BottomSheetRow(
                padding = padding,
                text = "Image",
                onClick = {
                    onShareImage()
                    showBottomSheet = false
                },
                icon = Icons.Outlined.Image,
            )
            CancelButton(modifier = Modifier.padding(padding)) { showBottomSheet = false }
        }
    }
}

@Composable
fun CancelButton(modifier: Modifier = Modifier, onClick: () -> Unit) {

    FilledTonalButton(modifier = modifier.fillMaxWidth().height(50.dp), onClick = onClick) {
        Text("Cancel")
    }
}

@Composable
fun TitleRow(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text("Share as", style = MaterialTheme.typography.titleMediumEmphasized)
    }
}

@Composable
fun BottomSheetRow(padding: Dp, text: String, onClick: () -> Unit, icon: ImageVector) {
    HorizontalDivider()
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier =
            Modifier.padding(padding)
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = interactionSource,
                )
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(padding),
    ) {
        Icon(icon, contentDescription = "Share type icon")
        Text(text)
    }
}

@Preview
@Composable
fun PreviewShareIcon() {
    AppTheme { ShareIcon(onShareText = {}, onShareImage = {}) }
}

@Composable
private fun ReadyResultScreenContent(
    onNavigateHome: (HomeRoute) -> Unit,
    onNavigateDescriptiveResult: (DescriptiveResultRoute) -> Unit,
    uiState: Ready,
    graphicsLayer: GraphicsLayer,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val maxCardSize = 175.dp
        val padding = 25.dp
        ScreenShotContainer(graphicsLayer = graphicsLayer, paddingValues = PaddingValues(padding)) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().height(maxCardSize),
            ) {
                HearingLossCard(
                    lossDbHl = uiState.generalRightHearingLoss,
                    side = SideUiState.RIGHT,
                    modifier = Modifier.weight(10f).widthIn(max = maxCardSize),
                    describedLossLevel = uiState.describedLeftHearingLoss,
                )
                Spacer(modifier = Modifier.weight(1f))
                HearingLossCard(
                    lossDbHl = uiState.generalLeftHearingLoss,
                    side = SideUiState.LEFT,
                    modifier = Modifier.weight(10f).widthIn(max = maxCardSize),
                    describedLossLevel = uiState.describedRightHearingLoss,
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Audiogram(
                leftAC = uiState.leftAC,
                rightAC = uiState.rightAC,
                modifier = Modifier.fillMaxWidth().height(300.dp),
            )
        }
        DoneButton(
            modifier = Modifier.align(Alignment.BottomCenter).padding(padding),
            onClick = { onNavigateHome(HomeRoute) },
        )
    }
}

@Composable
fun ScreenShotContainer(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    graphicsLayer: GraphicsLayer,
    content: @Composable () -> Unit,
) {
        Column(
            modifier =
                modifier
                    .drawWithContent {
                        graphicsLayer.record { this@drawWithContent.drawContent() }
                        drawLayer(graphicsLayer)
                    }
                    .background(MaterialTheme.colorScheme.surface).padding(paddingValues)
        ) {
            content()
        }
}

@Preview
@Composable
fun PreviewResultScreen() {
    val uiState by remember {
        mutableStateOf(
            Ready(
                generalLeftHearingLoss = 46,
                generalRightHearingLoss = 30,
                leftAC =
                    hashMapOf(
                        125 to 90,
                        250 to 50,
                        500 to 20,
                        1000 to 20,
                        2000 to 30,
                        4000 to 55,
                        8000 to 35,
                    ),
                rightAC =
                    hashMapOf(
                        125 to 30,
                        250 to 30,
                        500 to 5,
                        1000 to 0,
                        2000 to 0,
                        4000 to 5,
                        8000 to 5,
                    ),
                describedRightHearingLoss = "Normal",
                describedLeftHearingLoss = "Profound",
            )
        )
    }
    AppTheme {
        AudiosenseScaffold(
            contentPadding = PaddingValues(),
            topBar = {
                AudiosenseAppBar(title = "Results", canNavigateBack = true, onNavigateBack = {})
            },
        ) {
            val graphicsLayer = rememberGraphicsLayer()
            ReadyResultScreenContent({}, {}, uiState, graphicsLayer = graphicsLayer)
        }
    }
}
