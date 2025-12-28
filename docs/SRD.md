# Software Requirements Document (SRD)
## AudioSense - Hearing Test Application

| **Document Information** |                                      |
|--------------------------|--------------------------------------|
| **Project Name**         | AudioSense                           |
| **Version**              | 1.0                                  |
| **Date**                 | December 28, 2025                    |
| **Status**               | Under Active Development             |

---

## 1. Introduction

### 1.1 Purpose
This document specifies the software requirements for AudioSense, a cross-platform mobile audiometry application designed to provide accessible hearing level measurements.

### 1.2 Scope
AudioSense is a native mobile application for Android and iOS that enables users to conduct pure-tone audiometry tests, view results, and understand their hearing health.

### 1.3 Definitions & Acronyms

| **Term**       | **Definition**                                                                 |
|----------------|--------------------------------------------------------------------------------|
| Audiometry     | The measurement of hearing ability                                             |
| Pure Tone      | A sound with a single frequency                                                |
| dB HL          | Decibels Hearing Level - unit for measuring hearing threshold                  |
| Audiogram      | A graph showing hearing thresholds at different frequencies                    |
| Calibration    | Process of adjusting measurements for specific headphone characteristics       |
| Threshold      | The softest sound a person can hear at a specific frequency                    |

---

## 2. Overall Description

### 2.1 Product Perspective

| **Aspect**           | **Description**                                                        |
|----------------------|------------------------------------------------------------------------|
| **Platform**         | Android & iOS (Kotlin Multiplatform)                                   |
| **Architecture**     | Clean Architecture with MVVM pattern                                   |
| **UI Framework**     | Jetpack Compose / Compose Multiplatform                                |
| **Design System**    | Material 3 Expressive                                                  |
| **Data Storage**     | Local database (Room)                                                  |
| **Connectivity**     | Optional server sync for headphone data                                |

### 2.2 User Classes

| **User Class** | **Description**                                    | **Access Level**     |
|----------------|----------------------------------------------------|----------------------|
| End User       | Individual conducting hearing self-assessment      | Full app access      |
| Guest User     | User without account                               | Local features only  |

### 2.3 Operating Environment

| **Platform** | **Minimum Version** | **Requirements**                    |
|--------------|---------------------|-------------------------------------|
| Android      | API 24 (Android 7)  | Microphone, Audio output            |
| iOS          | iOS 14+             | Microphone, Audio output            |

---

## 3. Functional Requirements

### 3.1 Home Module (FR-HOME)

| **ID**     | **Requirement**                    | **Description**                                                     | **Priority** |
|------------|------------------------------------|---------------------------------------------------------------------|--------------|
| FR-HOME-01 | View Test History                  | User shall be able to view a list of all previous hearing tests    | High         |
| FR-HOME-02 | Delete Test                        | User shall be able to delete a specific test from history          | Medium       |
| FR-HOME-03 | View Test Details                  | User shall be able to view detailed results of a past test         | High         |

### 3.2 Hearing Test Module (FR-TEST)

| **ID**     | **Requirement**                    | **Description**                                                     | **Priority** |
|------------|------------------------------------|---------------------------------------------------------------------|--------------|
| FR-TEST-01 | Start New Test                     | User shall be able to initiate a new hearing test                  | High         |
| FR-TEST-02 | Enter Personal Details             | User shall enter age and relevant information before testing       | High         |
| FR-TEST-03 | Select Headphone                   | User shall select their headphone model for calibration            | High         |
| FR-TEST-04 | Perform Pure Tone Audiometry       | System shall play pure tones at varying frequencies and amplitudes | High         |
| FR-TEST-05 | Respond to Sound                   | User shall indicate when they hear a sound                         | High         |
| FR-TEST-06 | View Test Progress                 | User shall see current progress during the test                    | Medium       |
| FR-TEST-07 | Complete Test                      | System shall finalize and save results upon test completion        | High         |
| FR-TEST-08 | Test Both Ears                     | System shall test left and right ears separately                   | High         |
| FR-TEST-09 | Adaptive Threshold Algorithm       | System shall use ascending/descending method to find thresholds    | High         |

### 3.3 Results Module (FR-RESULT)

| **ID**       | **Requirement**                  | **Description**                                                     | **Priority** |
|--------------|----------------------------------|---------------------------------------------------------------------|--------------|
| FR-RESULT-01 | View Audiogram                   | User shall view hearing thresholds on an audiogram chart           | High         |
| FR-RESULT-02 | View Hearing Loss Analysis       | System shall provide interpretation of results                     | High         |
| FR-RESULT-03 | Share Results                    | User shall be able to share test results                           | Low          |

### 3.4 Calibration Module (FR-CAL)

| **ID**    | **Requirement**                   | **Description**                                                     | **Priority** |
|-----------|-----------------------------------|---------------------------------------------------------------------|--------------|
| FR-CAL-01 | Calibrate Headphone               | User shall be able to calibrate their specific headphone model     | High         |
| FR-CAL-02 | Add New Headphone                 | User shall be able to add a new headphone with calibration data    | Medium       |
| FR-CAL-03 | View Calibration Coefficients    | User shall view calibration values for their headphone             | Low          |

### 3.5 Device Management Module (FR-DEV)

| **ID**    | **Requirement**                   | **Description**                                                     | **Priority** |
|-----------|-----------------------------------|---------------------------------------------------------------------|--------------|
| FR-DEV-01 | View Available Headphones         | User shall see list of supported headphone models                  | High         |
| FR-DEV-02 | Select Authenticated Headphone    | User shall select from verified headphone models                   | High         |
| FR-DEV-03 | Delete Headphone                  | User shall be able to remove a headphone from their list           | Low          |

### 3.6 Data Sync Module (FR-SYNC)

| **ID**     | **Requirement**                   | **Description**                                                     | **Priority** |
|------------|-----------------------------------|---------------------------------------------------------------------|--------------|
| FR-SYNC-01 | Sync Headphones from Server       | System shall download headphone calibration data from server       | Medium       |
| FR-SYNC-02 | Upload Test Results               | User shall be able to upload results to server (optional)          | Low          |
| FR-SYNC-03 | Authenticate with Server          | System shall authenticate before syncing data                      | Medium       |

---

## 4. Non-Functional Requirements

### 4.1 Performance Requirements

| **ID**     | **Requirement**                   | **Metric**                                                          | **Priority** |
|------------|-----------------------------------|---------------------------------------------------------------------|--------------|
| NFR-PERF-01| App Launch Time                   | Application shall launch within 3 seconds                          | High         |
| NFR-PERF-02| Audio Latency                     | Audio playback latency shall be < 50ms                             | High         |
| NFR-PERF-03| Test Duration                     | Complete test shall take < 10 minutes                              | Medium       |
| NFR-PERF-04| Response Time                     | UI shall respond to user input within 100ms                        | High         |

### 4.2 Usability Requirements

| **ID**     | **Requirement**                   | **Description**                                                     | **Priority** |
|------------|-----------------------------------|---------------------------------------------------------------------|--------------|
| NFR-USE-01 | Intuitive Interface               | User shall complete test without training                          | High         |
| NFR-USE-02 | Clear Instructions                | All steps shall have clear, simple instructions                    | High         |
| NFR-USE-03 | Accessibility                     | App shall support large text and screen readers                    | Medium       |
| NFR-USE-04 | Multi-language Support            | App shall support multiple languages                               | Medium       |

### 4.3 Reliability Requirements

| **ID**     | **Requirement**                   | **Description**                                                     | **Priority** |
|------------|-----------------------------------|---------------------------------------------------------------------|--------------|
| NFR-REL-01 | Data Persistence                  | Test results shall be saved locally and persist across sessions    | High         |
| NFR-REL-02 | Crash Recovery                    | App shall recover gracefully from unexpected errors                | High         |
| NFR-REL-03 | Offline Operation                 | Core functionality shall work without internet connection          | High         |

### 4.4 Security Requirements

| **ID**     | **Requirement**                   | **Description**                                                     | **Priority** |
|------------|-----------------------------------|---------------------------------------------------------------------|--------------|
| NFR-SEC-01 | Data Privacy                      | Personal health data shall be stored locally only                  | High         |
| NFR-SEC-02 | Secure Communication              | Server communication shall use HTTPS encryption                    | High         |
| NFR-SEC-03 | No Unauthorized Access            | App shall not share data without user consent                      | High         |

### 4.5 Maintainability Requirements

| **ID**     | **Requirement**                   | **Description**                                                     | **Priority** |
|------------|-----------------------------------|---------------------------------------------------------------------|--------------|
| NFR-MAIN-01| Modular Architecture              | Code shall follow clean architecture principles                    | High         |
| NFR-MAIN-02| Logging                           | System shall log important events for debugging                    | Medium       |
| NFR-MAIN-03| Unit Testing                      | Core logic shall have unit test coverage                           | High         |

---

## 5. System Features

### 5.1 Pure Tone Audiometry Engine

| **Feature**              | **Description**                                                              |
|--------------------------|------------------------------------------------------------------------------|
| Frequency Range          | Test frequencies: 250Hz, 500Hz, 1000Hz, 2000Hz, 4000Hz, 8000Hz              |
| Amplitude Range          | -10 dB HL to 90 dB HL                                                        |
| Step Size                | Initial: 20 dB, Fine: 5 dB                                                   |
| Algorithm                | Modified Hughson-Westlake (ascending method with reversals)                  |
| Ear Testing              | Sequential testing of left and right ears                                    |

### 5.2 Noise Meter

| **Feature**              | **Description**                                                              |
|--------------------------|------------------------------------------------------------------------------|
| Ambient Noise Detection  | Measure ambient noise level before testing                                   |
| Threshold Warning        | Alert user if environment is too noisy                                       |
| Recommendation           | Suggest quiet environment for accurate results                               |

### 5.3 Results Interpretation

| **Hearing Level (dB HL)** | **Classification**        | **Description**                               |
|---------------------------|---------------------------|-----------------------------------------------|
| -10 to 25                 | Normal                    | No significant hearing difficulty             |
| 26 to 40                  | Mild Loss                 | Difficulty with soft speech                   |
| 41 to 55                  | Moderate Loss             | Difficulty with normal conversation           |
| 56 to 70                  | Moderately Severe Loss    | Difficulty with loud speech                   |
| 71 to 90                  | Severe Loss               | Only very loud sounds audible                 |
| 90+                       | Profound Loss             | May not hear most sounds                      |

---

## 6. External Interface Requirements

### 6.1 User Interfaces

| **Screen**               | **Purpose**                                                                  |
|--------------------------|------------------------------------------------------------------------------|
| Home Screen              | Display test history, start new test                                         |
| Personal Details Screen  | Collect user information                                                     |
| Headphone Selection      | Select and configure headphone                                               |
| Test Screen              | Conduct audiometry test with response button                                 |
| Progress Screen          | Show test progress indicator                                                 |
| Results Screen           | Display audiogram and analysis                                               |
| Settings Screen          | Configure theme, language, preferences                                       |

### 6.2 Hardware Interfaces

| **Hardware**             | **Purpose**                          | **Requirement**                          |
|--------------------------|--------------------------------------|------------------------------------------|
| Audio Output             | Play pure tone sounds                | Stereo output (left/right channels)      |
| Microphone               | Ambient noise measurement            | Access to device microphone              |
| Display                  | Visual interface                     | Touch-enabled screen                     |

### 6.3 Software Interfaces

| **Interface**            | **Description**                                                              |
|--------------------------|------------------------------------------------------------------------------|
| Room Database            | Local storage for test results and headphone data                            |
| Audio API                | Platform-specific audio playback (Android AudioTrack / iOS AVFoundation)    |
| REST API                 | Optional server communication for headphone sync                             |

---

## 7. Constraints

| **Constraint**           | **Description**                                                              |
|--------------------------|------------------------------------------------------------------------------|
| Platform Limitation      | Limited to Android and iOS platforms                                         |
| Headphone Dependency     | Accurate results require calibrated headphones                               |
| Environment Dependency   | Quiet environment required for valid results                                 |
| Not Medical Device       | Results are for informational purposes only, not clinical diagnosis          |

---

## 8. Assumptions and Dependencies

| **Type**       | **Description**                                                                    |
|----------------|------------------------------------------------------------------------------------|
| Assumption     | User has access to compatible headphones                                           |
| Assumption     | User can find a quiet environment for testing                                      |
| Assumption     | User can hear at least some sounds within the test range                           |
| Dependency     | Kotlin Multiplatform framework stability                                           |
| Dependency     | Jetpack Compose / Compose Multiplatform libraries                                  |
| Dependency     | Room database for local persistence                                                |
| Dependency     | Koin for dependency injection                                                      |

---

## 9. Appendix

### 9.1 Use Case Diagram Reference
See `use-case-diagram.mmd` for visual representation of system use cases.

### 9.2 Technology Stack

| **Component**            | **Technology**                                                               |
|--------------------------|------------------------------------------------------------------------------|
| Language                 | Kotlin                                                                       |
| Framework                | Kotlin Multiplatform                                                         |
| UI                       | Jetpack Compose / Compose Multiplatform                                      |
| Navigation               | androidx.navigation                                                          |
| Dependency Injection     | Koin                                                                         |
| Database                 | Room                                                                         |
| Logging                  | Kermit                                                                       |

---

**Document End**

