# AudioSense - Requirements

## As a User

- As a user, I want to take a hearing test using my mobile device so that I can assess my hearing levels conveniently
- As a user, I want to select my headphone model before testing so that results are calibrated accurately
- As a user, I want the app to check my environment noise level so that I can ensure accurate test results
- As a user, I want to view my audiogram results visually so that I can understand my hearing levels
- As a user, I want to receive easy-to-understand explanations of my results so that I know what they mean in everyday situations
- As a user, I want to save my test results so that I can track my hearing over time
- As a user, I want to share my audiogram results with healthcare professionals so that I can get proper medical advice
- As a user, I want to customize app settings (theme, language) so that I can personalize my experience
- As a user, I want my audiogram data to be uploaded to a backend API so that my results can be accessed by authorized healthcare professionals

## As an Admin

- As an admin, I want to manage headphone calibration data through an admin dashboard so that users get accurate results
- As an admin, I want to add new headphone models to the system so that more devices are supported
- As an admin, I want to update existing headphone calibration profiles so that accuracy is maintained
- As an admin, I want to remove outdated headphone models so that users don't select unsupported devices
- As an admin, I want to view system usage statistics so that I can understand how the app is being used
- As an admin, I want to manage user data access permissions so that privacy is protected

## As an Audiologist

- As an audiologist, I want to view patient audiogram results through a secure interface so that I can review their hearing health
- As an audiologist, I want to access patient test history so that I can track hearing changes over time
- As an audiologist, I want to export audiogram data in standard formats so that I can integrate with other medical systems
- As an audiologist, I want to add professional notes to patient audiograms so that I can document my assessments
- As an audiologist, I want to validate the accuracy of test results so that I can trust the data for clinical decisions

## Backend API Requirements

### Audiogram Data Management
- **POST** `/api/audiograms` - Upload audiogram test results from mobile app
  - Include: user ID, test date, frequency/threshold data, headphone model, environment noise level
  - Return: audiogram ID, upload confirmation

- **GET** `/api/audiograms/{userId}` - Retrieve user's audiogram history
  - Authorization: User or assigned audiologist
  - Return: List of audiograms with metadata

- **GET** `/api/audiograms/{audiogramId}` - Retrieve specific audiogram details
  - Authorization: User or assigned audiologist
  - Return: Complete audiogram data

### Headphone Management
- **GET** `/api/headphones` - Fetch available headphone models and calibration data
  - Return: List of supported headphones with calibration profiles

- **POST** `/api/headphones` - Add new headphone model (Admin only)
  - Include: model name, manufacturer, calibration data
  - Return: headphone ID, confirmation

- **PUT** `/api/headphones/{headphoneId}` - Update headphone calibration (Admin only)
  - Include: updated calibration data
  - Return: confirmation

- **DELETE** `/api/headphones/{headphoneId}` - Remove headphone model (Admin only)
  - Return: confirmation

### Admin Dashboard API
- **GET** `/api/admin/statistics` - Retrieve system usage statistics
  - Authorization: Admin only
  - Return: user count, test count, popular headphones

- **GET** `/api/admin/headphones` - Admin view of all headphone data
  - Authorization: Admin only
  - Return: Complete headphone list with usage stats

### Audiologist Portal API
- **GET** `/api/audiologist/patients` - List assigned patients
  - Authorization: Audiologist only
  - Return: List of patients with recent test summary

- **GET** `/api/audiologist/patients/{userId}/audiograms` - Patient audiogram history
  - Authorization: Assigned audiologist only
  - Return: Complete audiogram history for patient

- **POST** `/api/audiologist/notes/{audiogramId}` - Add professional notes
  - Authorization: Assigned audiologist only
  - Include: clinical notes, recommendations
  - Return: confirmation
