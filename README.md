# Experiment 12 – Access Web Services using HTTP Requests in Android

📌 **Description**
This experiment is developed using Android Studio as part of the Mobile Application Development Lab. It demonstrates how to access web services and consume REST APIs using HTTP requests in an Android application.

The application utilizes **Retrofit** and **Gson** for seamless HTTP networking and JSON deserialization, combined with **Kotlin Coroutines** for asynchronous background execution. The user interface features a modern Material Design layout with a `RecyclerView`, custom `CardView` items, and a `SwipeRefreshLayout` for pull-to-refresh functionality.

👨‍💻 **Author**
*   **Name:** Debayan Saha
*   **Subject:** Mobile Application Development Lab
*   **USN:** 25MCAR0129

🚀 **Features**
*   Performs network HTTP GET requests to fetch remote data.
*   Uses **Retrofit 2** as the type-safe HTTP client.
*   Parses JSON responses into Kotlin data classes using **Gson Converter**.
*   Implements **Kotlin Coroutines** (`Dispatchers.IO` / `Dispatchers.Main`) for non-blocking network calls.
*   Features a modern **Material 3 UI** with:
    *   `RecyclerView` for efficient list rendering.
    *   `CardView` layouts with rounded corners and shadows (`item_post.xml`).
    *   `SwipeRefreshLayout` for pull-to-refresh capability.
    *   `ProgressBar` for loading indicators.
*   Includes internet permission handling in `AndroidManifest.xml`.

🛠️ **Technologies Used**
*   Android Studio
*   Kotlin & Kotlin Coroutines
*   Retrofit 2 & Gson
*   RecyclerView & SwipeRefreshLayout
*   Material Design Components

📂 **Project Structure**
```text
exp12/
├── app/
│   ├── build.gradle.kts (Retrofit, Gson, and Coroutine dependencies)
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml (Internet permission)
│           ├── java/
│           │   └── com.example.myapplication/
│           │       ├── MainActivity.kt (Handles HTTP fetch and RecyclerView binding)
│           │       ├── ApiService.kt (Retrofit interface for API endpoints)
│           │       ├── Post.kt (Data model for JSON response)
│           │       └── PostAdapter.kt (Adapter for RecyclerView)
│           └── res/
│               ├── layout/
│               │   ├── activity_main.xml (Toolbar, SwipeRefreshLayout, RecyclerView)
│               │   └── item_post.xml (CardView item layout)
│               ├── values/
│               │   ├── colors.xml
│               │   ├── strings.xml
│               │   └── themes.xml
│               └── xml/
│                   ├── backup_rules.xml
│                   └── data_extraction_rules.xml
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle.kts
```

📸 **Output**
*(Screenshots of the fetched posts list to be added here)*