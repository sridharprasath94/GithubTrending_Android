# GitHub Trending

GitHub Trending is a modern Android application that showcases the most popular repositories on GitHub.  
The project demonstrates **modern Android development practices** including clean architecture, scalable data flow, offline caching, and paginated APIs.

The goal of the project is to build a **production‑ready architecture** using Jetpack components while keeping the UI responsive and the data layer robust.

---

## Overview

The application fetches trending repositories from the **GitHub Search API** and displays them in a smooth scrolling list.  
Repositories can be marked as **favorites**, which are persisted locally and pinned to the top of the list.

The project focuses on:

- Modern Android architecture
- Pagination with large datasets
- Offline caching strategies
- Clean separation of concerns

---

## Features

- View trending GitHub repositories
- Smooth infinite scrolling using Paging 3
- Mark repositories as favorites
- Favorites automatically appear at the top
- Pull‑to‑refresh support
- Offline caching with Room
- Repository search functionality
- Clean Material Design UI

---

## Architecture

The application follows **Clean Architecture + MVVM** principles to ensure scalability and maintainability.

```
UI (Compose / Views)
        │
        ▼
    ViewModel
        │
        ▼
UseCases (Domain Layer)
        │
        ▼
     Repository
   │           │
   ▼           ▼
Remote API   Local DB (Room)
```

### Key Principles

- **Single Source of Truth** using Room database
- **Repository Pattern** for data abstraction
- **UseCases** for business logic
- **Dependency Injection with Hilt**
- **Reactive data streams using Kotlin Flow**

---

## Tech Stack

### Language
- Kotlin

### Android Architecture
- MVVM
- Clean Architecture
- Repository Pattern

### Jetpack Libraries
- ViewModel
- Navigation
- Paging 3
- Room
- Hilt Dependency Injection

### Networking
- Retrofit
- OkHttp

### Asynchronous Programming
- Kotlin Coroutines
- Kotlin Flow

### UI
- Jetpack Compose
- Material Design 3
- Coil (Image loading)

---

## Pagination Strategy

The project uses **Paging 3 with RemoteMediator** to efficiently load repositories.

```
GitHub API
     │
     ▼
RemoteMediator
     │
     ▼
Room Database
     │
     ▼
PagingSource
     │
     ▼
    UI
```

Benefits of this approach:

- Smooth infinite scrolling
- Offline-first experience
- Reduced network requests
- Reliable state restoration

---

## Caching Strategy

Repositories are cached locally using **Room**.

Advantages:

- Faster data loading
- Offline access to previously loaded repositories
- Reduced API usage

Favorites are stored locally and remain persistent even after refreshing data.

---

## API

The application uses the **GitHub Search API** to fetch popular repositories.

Example request:

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

Some potential improvements planned for the project:

- Repository detail screen
- Improved trending algorithms
- UI enhancements and animations
- Dark mode support
- Advanced filtering and sorting
- Better error handling and retry mechanisms
- Optional GitHub authentication

---