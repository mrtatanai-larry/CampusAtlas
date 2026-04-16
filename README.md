# CampusAtlas

CampusAtlas by TechIT Easy is an Android application designed to help users navigate, bookmark, and manage campus locations. Built with modern Android development practices, this app features an interactive map, data tables, and a dedicated admin mode.

## Features

- **Interactive Map**: Powered by OpenStreetMap (`osmdroid`), allowing users to explore the campus layout seamlessly.
- **Admin Mode**: Special access mode configured during the first-time setup, giving administrators the ability to manage and view specialized map features.
- **Data Table View**: View and manage campus locations or records in a structured tabular format.
- **Bookmarks**: Save your favorite or frequently visited campus locations for quick access later.
- **Modern Onboarding**: Intuitive first-time setup flow that configures user preferences.
- **Fluid Animations**: Smooth screen transitions and animations enhancing the user experience.

## Tech Stack

This project is built using modern Android technologies:
- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Map Rendering**: OpenStreetMap (`osmdroid`)
- **Local Persistence**: Room Database
- **Navigation**: Jetpack Navigation Compose
- **Backend/Analytics**: Firebase (BOM & Analytics)

## Project Structure

- `AppMain.kt`: The core composable handling app-wide navigation, routing, and transitions.
- `ui/mainscreen/Mapview.kt`: Contains the primary OSMDroid map implementation.
- `ui/panels/`: Hosts supplemental screens such as `DataTableScreen`, `BookmarksScreen`, and `SettingsScreen`.
- `ui/setup/`: Contains the `FirstSetupScreen` for onboarding.

## Getting Started

### Prerequisites
- Android Studio (Version that supports Jetpack Compose)
- **Minimum SDK**: 26 (Android 8.0 Oreo)
- **Target SDK**: 36

### Installation
1. Open the project folder (`TechIT Easy - CampusAtlas`) in Android Studio.
2. Wait for Android Studio to sync the Gradle build files.
3. Select your target device or emulator and press **Run** (`Shift + F10`).
