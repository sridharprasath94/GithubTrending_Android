

# GitHub Trending
Kotlin based Android application that showcases the **most popular trending repositories on GitHub**.  
The project focuses on **clean architecture, scalable data flow, and modern Android development practices**.

It demonstrates how to build a production-ready app using **Jetpack libraries, reactive data streams, offline caching, and paginated APIs**.

---

## Features

- Browse trending GitHub repositories
- Infinite scrolling with smooth pagination
- Mark repositories as **favorites**
- Favorites are **pinned to the top**
- Offline cache using Room
- Pull-to-refresh support
- Repository search functionality
- Clean and responsive Material UI

---

## Architecture

The project follows **Clean Architecture principles** with clear separation of responsibilities:

```
UI
│
├── ViewModel
│
├── UseCases (Domain Layer)
│
├── Repository
│
├── Local Data Source (Room)
│
└── Remote Data Source (GitHub API)
```

Key architectural concepts used:

- MVVM architecture
- Repository pattern
- Use case driven domain layer
- Single source of truth (Room database)
- Dependency Injection with Hilt

---

## Tech Stack

### Android
- Kotlin
- Jetpack Compose
- ViewModel
- Navigation Component
- Hilt Dependency Injection

### Data & Networking
- Retrofit
- OkHttp
- Room Database
- Kotlin Coroutines
- Kotlin Flow

### Pagination
- Paging 3
- RemoteMediator
- GitHub Search API

### UI
- Material Design 3
- Compose Icons
- Coil image loading

---

## Pagination Strategy

Trending repositories are loaded using **Paging 3 with RemoteMediator**.

```
GitHub API
     │
     ▼
RemoteMediator
     │
     ▼
Room Database (Source of Truth)
     │
     ▼
PagingSource
     │
     ▼
UI
```

This approach provides:

- Smooth infinite scrolling
- Offline support
- Consistent data source
- Automatic retry and refresh

---

## Caching Strategy

Repositories are cached locally using **Room**.

Benefits:

- Faster loading after first fetch
- Offline browsing
- Reduced API calls

Favorites are stored locally and **persist across refreshes**.

---

## API

The application uses the **GitHub Search API**.

Example endpoint:

```
https://api.github.com/search/repositories?q=stars:>10000&sort=stars&order=desc&page=1&per_page=20
```

---

## Project Structure

```
app
│
├── data
│   ├── api
│   ├── database
│   ├── repository
│
├── domain
│   ├── model
│   ├── usecase
│
├── ui
│   ├── trending
│   ├── favorites
│   ├── search
│
└── di
```

---

## Future Improvements

Planned enhancements for the project:

- Improved GitHub trending algorithm
- Repository detail screen
- Dark mode support
- Advanced filtering
- UI animations and polish
- Better error handling and retry UI
- GitHub authentication (optional)
- Compose UI migration for all screens

---