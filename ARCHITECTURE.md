# معماری AudioSense

## MVI Pattern

```mermaid
graph LR
    A[View] -->|Intent| B[ViewModel]
    B -->|State| A
    B --> C[Repository]
    C --> D[Database]
```

## دیتابیس (Room)

```mermaid
erDiagram
    LocalHeadphone ||--o{ LocalTest : "1:N"
    
    LocalHeadphone {
        string id
        string model
        map calibrationCoefficients
    }
    
    LocalTest {
        string id
        map leftAC
        map rightAC
        string headphoneId
    }
```

**LocalHeadphone**: کالیبراسیون `Map<Int, Pair<Int,Int>>`  
**LocalTest**: نتایج شنوایی `Map<Int, Int>` با CASCADE DELETE

## معماری لایه‌ای

```mermaid
graph TB
    A[Presentation: Compose + ViewModel] --> B[Domain: Repository + UseCase]
    B --> C[Data: Room + API]
```

**Stack**: Kotlin Multiplatform، Compose، Room، Koin، Flow
