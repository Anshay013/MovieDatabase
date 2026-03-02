# Movie Data Base 🎬

A modern, professional Android application that allows users to explore trending and now-playing movies using the 
TMDB API. Built with Kotlin and the latest Android development best practices (2025-2026).

 🚀 Key Features

1. Hybrid Home Screen
- Trending & Now Playing: Real-time display of popular cinema content.
- Local-First Architecture: Data is always served from the local Room database for a seamless offline experience.
- Smart Sync: Pull-to-refresh mechanism that updates the local cache without losing user-specific data (like bookmarks).

2. Intelligent Search
- Hybrid Search: Provides instantaneous results from the local database while typing, combined with debounced (600ms) network results to minimize API overhead.
- Real-time Bookmarking: Users can bookmark movies directly from search results. If a movie is new to the system, it is automatically persisted to the local DB.

3. Immersive Movie Details
- Rich UI: Uses "CollapsingToolbarLayout" for a parallax backdrop effect.
- Deep Linking: Supports custom URI schemes (moviedb://movie/{id}) allowing external links to open the app directly to a specific movie.
- Interactive Actions: Direct bookmark toggling and movie sharing.

4. Saved Movies & Multi-Select
- Collection Management: A dedicated grid view for all bookmarked movies.
- Batch Sharing: Professional multi-select implementation using Contextual Action Mode (CAB). Long-press to enter selection mode, select multiple items, and share their details via the System Share sheet.
- State Persistence: Full UI state management using "SavedStateHandle", ensuring selections and headers survive screen rotations and theme changes (Light/Dark mode).

🛠 Technical Stack

- Language: Kotlin
- Architecture: MVVM (Model-View-ViewModel) + Repository Pattern
- Dependency Injection: Hilt (Dagger)
- Database: Room ORM
- Networking: Retrofit 3.0 + OkHttp 5.0
- Serialization: Kotlinx Serialization (Modern JSON handling)
- Concurreny: Kotlin Coroutines & Flow (Reactive data streams)
- Image Loading: Coil (with automatic memory and disk caching)
- UI Components: ViewBinding, Material Components, SwipeRefreshLayout, NestedScrollView



🏗 Architectural Highlights

Persistence Strategy (The "Upsert" Pattern)
The app implements a custom "upsert" logic in the Room DAO. When refreshing network data, the app updates movie details but preserves the user's "bookmarked" flag. This prevents data loss during synchronization.

Unidirectional Data Flow (UDF)
Each screen observes a single "uiState" Flow from its ViewModel. This ensures that the UI is always a direct reflection of the underlying data source, making the app highly predictable and easy to debug.

Secure Secret Management
Sensitive information like the "TMDB API Key" is managed via "gradle.properties" and injected into the app through "BuildConfig". The property file is ignored by Git, following security best practices for public repositories.


📂 Project Structure

- "ui/": Activities, Adapters, and custom UI components.
- 'data/viewmodel/": State-aware ViewModels handling business logic.
- "data/repository/": Data layer abstraction (Local vs. Remote sync).
- "database/": Room DB configuration, Entities, and DAOs.
- "movieapi/": Retrofit interfaces and DTOs.
- "di/": Hilt modules for Dependency Injection.


🏁 Getting Started

1. Clone the repository.
2. Add your TMDB API Key to "gradle.properties":
   properties
   MOVIE_API_KEY=your_api_key_here
   
3. Build and Run the app.


📄 License
This project was created for professional demonstration purposes. Created on March 1, 2025.
