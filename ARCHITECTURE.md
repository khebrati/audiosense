# معماری پروژه AudioSense

## مقدمه
AudioSense یک اپلیکیشن اندرویدی و iOS برای سنجش شنوایی است که با استفاده از Kotlin Multiplatform و Jetpack Compose ساخته شده است. این پروژه از معماری MVI (Model-View-Intent) و الگوی Clean Architecture برای جداسازی لایه‌های مختلف استفاده می‌کند.

## معماری MVI (Model-View-Intent)

پروژه از الگوی MVI برای مدیریت وضعیت UI و جریان داده‌ها استفاده می‌کند. در این الگو:

- **Model (State)**: وضعیت immutable که UI را نمایش می‌دهد
- **View**: Composable های Jetpack Compose که state را رندر می‌کنند
- **Intent (Action)**: رویدادهایی که کاربر یا سیستم تولید می‌کنند

### جریان MVI

```mermaid
graph LR
    A[View/Composable] -->|User Action/Intent| B[ViewModel]
    B -->|Use Case/Repository| C[Domain Layer]
    C -->|Data| D[Data Layer]
    D -->|Result| C
    C -->|Domain Model| B
    B -->|Update State| E[UI State]
    E -->|Recompose| A
    
    style A fill:#e1f5ff
    style B fill:#fff3e0
    style C fill:#f3e5f5
    style D fill:#e8f5e9
    style E fill:#fff9c4
```

### نمونه‌هایی از State و Intent

```mermaid
classDiagram
    class TestUiState {
        +Float progress
        +SideUiState side
        +Boolean isCompleted
    }
    
    class HomeUiState {
        +TimeOfDay currentTimeOfDay
        +List~CompactRecord~ testHistory
    }
    
    class CalibrationUiState {
        +Int frequency
        +Int volume
        +SideUiState side
    }
    
    class TestUiAction {
        <<sealed interface>>
        OnClick
    }
    
    class HomeIntent {
        <<sealed interface>>
        SelectForDelete
        OnClick
        CancelDelete
        Delete
    }
    
    class CalibrationUiAction {
        <<sealed interface>>
        SetFrequency
        SetVolume
        PlaySound
        Save
    }
```

## ساختار دیتابیس (Room)

پروژه از Room Database برای ذخیره‌سازی محلی داده‌ها استفاده می‌کند. دیتابیس شامل دو جدول اصلی است:

### نمودار ER دیتابیس

```mermaid
erDiagram
    LocalHeadphone ||--o{ LocalTest : "has many"
    
    LocalHeadphone {
        string id PK
        string model
        map calibrationCoefficients
    }
    
    LocalTest {
        string id PK
        instant dateTime
        int noiseDuringTest
        map leftAC
        map rightAC
        string headphoneId FK
        string personName
        int personAge
        boolean hasHearingAidExperience
    }
```

### توضیحات جداول

**LocalHeadphone**: هدفون‌های کالیبره شده
- `id`: شناسه یکتا
- `model`: مدل هدفون (مثلاً "Galaxy Buds FE")
- `calibrationCoefficients`: ضرایب کالیبراسیون به ازای هر فرکانس
  - Format: `Map<Int, Pair<Int, Int>>` 
  - Key: فرکانس (125, 250, 500, 1000, 2000, 4000, 8000 Hz)
  - Value: `(playedVolume, measuredVolume)` به دسی‌بل (dB)

**LocalTest**: تست‌های شنوایی انجام شده
- `id`: شناسه یکتا
- `dateTime`: زمان انجام تست
- `noiseDuringTest`: میزان نویز محیط در حین تست
- `leftAC/rightAC`: منحنی شنوایی گوش چپ و راست
  - Format: `Map<Int, Int>` (فرکانس → دسی‌بل)
- `headphoneId`: ارجاع به هدفون استفاده شده
- سایر اطلاعات دموگرافیک

### DAO ها

```mermaid
classDiagram
    class HeadphoneDao {
        +add(LocalHeadphone)
        +upsert(LocalHeadphone)
        +observeAll() Flow
        +getAll() List
        +delete(String id)
    }
    
    class TestDao {
        +add(LocalTest)
        +deleteById(String id)
        +deleteAll()
    }
    
    class TestHeadphoneDao {
        +observeAll() Flow~Map~
        +getAll() Map
    }
    
    AppDatabase --> HeadphoneDao
    AppDatabase --> TestDao
    AppDatabase --> TestHeadphoneDao
```

**TestHeadphoneDao** از Join Query استفاده می‌کند تا داده‌های هدفون و تست‌های مربوطه را با هم برگرداند. این ساختار امکان نمایش تاریخچه تست‌ها را گروه‌بندی شده بر اساس هدفون استفاده شده فراهم می‌کند:
```kotlin
Map<LocalHeadphone, List<LocalTest>>
```

### Type Converters

برای ذخیره‌سازی ساختارهای پیچیده (Map, Instant) در SQLite:
- `mapPairToString` / `stringToMapPair`: تبدیل `Map<Int, Pair<Int,Int>>` به JSON
- `mapToString` / `stringToMap`: تبدیل `Map<Int, Int>` به JSON  
- `instantToString` / `stringToInstant`: تبدیل Kotlin Instant به ISO 8601 string

## معماری سه لایه (Clean Architecture)

پروژه از Clean Architecture با سه لایه اصلی استفاده می‌کند:

```mermaid
graph TB
    subgraph Presentation["لایه Presentation"]
        A[Screens/Composables]
        B[ViewModels]
        C[UI States]
        D[UI Actions/Intents]
        A --> B
        B --> C
        A --> D
        D --> B
    end
    
    subgraph Domain["لایه Domain"]
        E[Repository Interfaces]
        F[Domain Models]
        G[Use Cases]
        E --> F
        G --> E
        G --> F
    end
    
    subgraph Data["لایه Data"]
        H[Repository Implementations]
        I[Local Data Source]
        J[Remote Data Source]
        K[Room Database]
        L[API Client]
        H --> I
        H --> J
        I --> K
        J --> L
    end
    
    Presentation --> Domain
    Domain --> Data
    
    style Presentation fill:#e3f2fd
    style Domain fill:#f3e5f5
    style Data fill:#e8f5e9
```

### لایه Presentation
- **Screens**: صفحات UI با Jetpack Compose
  - `HomeScreen`: نمایش لیست تست‌های قبلی
  - `TestScreen`: انجام تست شنوایی
  - `ResultScreen`: نمایش نتایج آدیوگرام
  - `CalibrationScreen`: کالیبراسیون هدفون
  - `SelectDeviceScreen`: انتخاب هدفون
  
- **ViewModels**: مدیریت state و business logic
  - از Koin برای dependency injection استفاده می‌شود
  - هر ViewModel با یک Screen متناظر است

### لایه Domain
- **Repository Interfaces**: 
  - `TestRepository`: مدیریت CRUD تست‌ها با پشتیبانی از Flow
  - `HeadphoneRepository`: مدیریت CRUD هدفون‌ها + همگام‌سازی با سرور

- **Models**: مدل‌های خالص بدون وابستگی به فریمورک
  - `Test`, `Headphone`, `VolumeRecordPerFrequency`

- **Use Cases**: منطق کسب‌وکار
  - `PureToneAudiometry`: مدیریت منطق تست صوتی
  - `HeadphoneCalibrator`: محاسبات کالیبراسیون
  - `AudiogramSerializer`: export و اشتراک‌گذاری نتایج
  - `SoundPlayer`: پخش صوت
  - `AudiometryPCMGenerator`: تولید سیگنال صوتی

### لایه Data
- **Repository Implementations**: پیاده‌سازی interface های Domain
  
- **Local Data Source**: 
  - Room Database با DAO ها
  - Entity های LocalTest و LocalHeadphone
  - Type Converters برای ذخیره ساختارهای پیچیده

- **Remote Data Source**:
  - `HeadphoneFetcher`: دریافت لیست هدفون‌های کالیبره شده از API
  - `TokenManager`: مدیریت توکن احراز هویت

## جریان داده در یک نمونه عملی

مثال: نمایش لیست تست‌های قبلی در `HomeScreen`

```mermaid
sequenceDiagram
    participant UI as HomeScreen
    participant VM as HomeViewModel
    participant Repo as TestRepository
    participant DAO as TestHeadphoneDao
    participant DB as Room Database
    
    UI->>VM: Collect testHistory StateFlow
    VM->>Repo: observeAll() Flow
    Repo->>DAO: observeAll() Flow
    DAO->>DB: SELECT with JOIN
    DB-->>DAO: Map<LocalHeadphone, List<LocalTest>>
    DAO-->>Repo: Flow<Map<...>>
    Repo-->>VM: Flow<List<Test>> (mapped to domain)
    VM-->>UI: HomeUiState(testHistory)
    UI->>UI: Recompose with new state
```

### نقش Koin (Dependency Injection)

```mermaid
graph TD
    A[Koin Module] --> B[Database Instance]
    A --> C[DAOs]
    A --> D[Repositories]
    A --> E[ViewModels]
    
    B --> C
    C --> D
    D --> E
    
    style A fill:#ffecb3
    style B fill:#c8e6c9
    style C fill:#b3e5fc
    style D fill:#f8bbd0
    style E fill:#d1c4e9
```

تمام dependency ها در `Module.kt` با Koin تعریف می‌شوند:
```kotlin
single<AppDatabase> { getRoomDatabase(...) }
single<HeadphoneDao> { get<AppDatabase>().headphoneDao() }
single<TestRepository> { TestRepositoryImpl(...) }
viewModelOf(::HomeViewModel)
```

## نقاط قوت معماری

1. **جداسازی واضح**: هر لایه مسئولیت مشخصی دارد
2. **تست‌پذیری**: با استفاده از interface ها، mock کردن آسان است
3. **Reactive**: استفاده از Flow برای به‌روزرسانی خودکار UI
4. **Type Safety**: استفاده از sealed class ها برای State و Intent
5. **Single Source of Truth**: State در ViewModel مدیریت می‌شود
6. **Multiplatform**: کد مشترک بین Android و iOS

## تکنولوژی‌های استفاده شده

- **Kotlin Multiplatform**: اشتراک کد بین پلتفرم‌ها
- **Jetpack Compose**: UI declarative
- **Room**: دیتابیس محلی
- **Koin**: Dependency injection
- **Coroutines + Flow**: برنامه‌نویسی ناهمزمان
- **Kotlinx Serialization**: سریالیزاسیون JSON
