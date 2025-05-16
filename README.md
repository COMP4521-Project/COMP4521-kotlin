# COMP4521 USTrade

USTrade is a modern Android application built with Kotlin and Jetpack Compose, designed to provide a seamless trading experience. The application leverages Firebase for backend services and follows modern Android development practices.

## Features

- Modern UI built with Jetpack Compose
- Firebase Authentication for secure user management
- Real-time data storage using Firebase Firestore
- Push notifications support via Firebase Cloud Messaging
- Image storage using Firebase Storage
- Camera integration for photo capture
- Material Design 3 components
- Navigation using Jetpack Navigation Compose
- Animation support using Lottie

## Technical Stack

- **Language**: Kotlin 1.8.22
- **Minimum SDK**: 34 (Android 14)
- **Target SDK**: 34
- **UI Framework**: Jetpack Compose
- **Backend Services**: Firebase
  - Authentication
  - Firestore
  - Cloud Messaging
  - Storage
- **Architecture Components**:
  - Navigation Compose
  - CameraX
  - Material3
  - Coil for image loading
  - Accompanist Permissions

## Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 1.8 or later
- Android SDK 34 or later
- Google Firebase account

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/COMP4521-kotlin.git
   ```

2. Open the project in Android Studio

3. Set up Firebase:
   - Create a new Firebase project in the [Firebase Console](https://console.firebase.google.com)
   - Add your Android app to the Firebase project
   - Download the `google-services.json` file and place it in the `app` directory
   - Enable the required Firebase services (Authentication, Firestore, Storage, Cloud Messaging)

4. Sync the project with Gradle files

5. Build and run the application

## Building the Project

1. Open the project in Android Studio
2. Wait for the Gradle sync to complete
3. Click on "Run" (green play button) or press Shift+F10

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/        # Kotlin source files
│   │   ├── res/         # Resources
│   │   └── AndroidManifest.xml
│   └── test/           # Unit tests
├── build.gradle        # App-level build configuration
└── google-services.json # Firebase configuration
```

## Dependencies

The project uses several key dependencies:

- Jetpack Compose for UI
- Firebase BOM (Bill of Materials) for backend services
- CameraX for camera functionality
- Navigation Compose for navigation
- Material3 for modern UI components
- Coil for image loading
- Lottie for animations
