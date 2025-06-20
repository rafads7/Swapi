# Star Wars Planets Viewer

This is an Android application that fetches and displays a list of planets from the Star Wars API (SWAPI)

---

## ✨ Features

* ✅ Fetches and displays a list of planets from SWAPI.
* ✅ Caches the list of planets for basic offline access and a fast, responsive user experience.
* ✅ Tapping a planet navigates to a detailed view showing more information.
* ✅ Clean, multi-module architecture designed for scalability.
* ✅ Implemented with modern, native Android Jetpack libraries.
* ✅ Dependency Injection implemented with Hilt for testability and modularity.
* ✅ Smooth screen transitions for an enhanced user experience.
* ✅ Basic accessibility considerations.

---

## 🏗️ Architecture & Technical Stack

This project follows the principles of **Clean Architecture** and employs a **Single Source of Truth (SSOT)** pattern. The goal is to create a clear separation of concerns, making the app highly scalable and easy to maintain.

### Architecture
- **UI Layer**: Built entirely with **Jetpack Compose**. State is managed using the **MVI** pattern with ViewModels, `StateFlow`, and a `UiState` sealed interface to represent loading, success, and error states.
- **Domain Layer**: Contains the core business models (e.g., `PlanetSummaryModel`) and UseCases. This layer is independent of any framework, separating the "what" from the "how".
- **Data Layer**: Manages all data operations. The **Repository** is the single entry point for the Domain layer and orchestrates data from local (Room) and remote (Retrofit) data sources.

### Modules
The app is organized into a multi-module structure to promote scalability and separation of concerns:
- `:app` - The main application module that assembles all other modules.
- `:core:ui` - Contains shared Jetpack Compose elements.
- `:core:network` - Handles all networking logic using Retrofit.
- `:core:common` - For common utilities like DI dispatchers, a Clock interface, etc.
- `:feature:planets` - A self-contained feature module with its own UI, Domain, and Data layers for everything related to planets.

### 🛠 Tech Stack
- **UI**: Jetpack Compose
- **State Management**: MVI, `ViewModel`, `StateFlow`, `UiState`
- **Dependency Injection**: Hilt
- **Networking**: Retrofit & Kotlinx.Serialization
- **Asynchronous**: Kotlin Coroutines & Flow
- **Database / Caching**: Room
- **Navigation**: Jetpack Navigation for Compose (Type-Safe with `kotlinx.serialization`)

---

## 🚀 How to Build and Run

1.  Clone the repository: `git clone [your-repo-link]`
2.  Open the project in a recent version of Android Studio (e.g., Hedgehog or newer).
3.  Let Gradle sync the project dependencies.
4.  Build and run on an emulator or a physical device.

---

## 🔮 Future Improvements

This project provides a solid foundation. Given more time, the following improvements could be made:
-   **Pagination**: Implement pagination using the Paging 3 library to handle the full list of planets instead of just the first page.
