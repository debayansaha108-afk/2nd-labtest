# SmartExpenseTracker – Expense Reminder & Balance Management Application (Lab Exam 2)

📌 **Description**  
This application is developed using Android Studio as part of the Mobile Application Development Lab Examination (Lab Exam 2). It is a feature-rich, modern fintech-style application designed to help users manage their daily remaining account balance and ensure consistent updates through a persistent daily alarm system.

When the scheduled alarm time arrives, the application triggers a high-priority notification and initiates a Foreground Service that plays a continuous alarm sound. To stop the alarm sound, the user must enter their exact previously saved remaining balance into the update field and successfully verify it against local SQLite records.

👨‍💻 **Author**
*   **Name:** Debayan Saha
*   **Subject:** Mobile Application Development Lab Examination (Lab Exam 2)
*   **USN:** 25MCAR0129

🚀 **Features**
*   **Modern Fintech UI Design:** Stunning navy blue and purple gradient dashboard (`bg_balance_card.xml`, `bg_primary_button.xml`), rounded input fields, and clean typography.
*   **Daily Alarm & Continuous Sound:** Uses `AlarmManager`, `BroadcastReceiver` (`ReminderReceiver`), and a Foreground Service (`AlarmSoundService`) with `MediaPlayer` to ring continuously until verified.
*   **Strict Balance Verification System:** Compares newly entered balance inputs with the latest saved balance in SQLite. The alarm continues ringing until exact verification is achieved.
*   **Dual SQLite Database Storage (`DatabaseHelper.kt`):**
    *   *Balance Records Table*: Tracks balance history with dates and times.
    *   *Verification Records Table*: Logs successful and failed alarm verification attempts.
*   **Device Reboot Persistence (`BootReceiver.kt`):** Automatically restores and reschedules daily reminders after device reboots (`BOOT_COMPLETED`).
*   **Runtime Permissions:** Handles Android 13+ notification permissions (`POST_NOTIFICATIONS`) and exact alarm scheduling.

🛠️ **Technologies Used**
*   Android Studio & Kotlin
*   SQLite Database (`SQLiteOpenHelper`)
*   AlarmManager & BroadcastReceiver
*   Foreground Service (`MediaPlayer` audio playback)
*   NotificationManager & Notification Channels
*   SharedPreferences
*   XML Layouts (ConstraintLayout, CardView, RecyclerView)

📂 **Project Structure**
```text
SmartExpenseTracker/
├── app/
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml (Permissions, Services, Receivers)
│           ├── java/
│           │   └── com.example.myapplication/
│           │       ├── MainActivity.kt (Dashboard, Verification logic, TimePickerDialog)
│           │       ├── DatabaseHelper.kt (SQLite Balance & Verification tables)
│           │       ├── AlarmSoundService.kt (Foreground service for continuous alarm audio)
│           │       ├── ReminderReceiver.kt (Alarm trigger & Notification builder)
│           │       ├── BootReceiver.kt (Reboot alarm restorer)
│           │       └── HistoryAdapter.kt (RecyclerView history adapter)
│           └── res/
│               ├── drawable/
│               │   ├── bg_balance_card.xml
│               │   ├── bg_input.xml
│               │   ├── bg_primary_button.xml
│               │   └── bg_white_card.xml
│               ├── layout/
│               │   ├── activity_main.xml (Fintech Dashboard UI)
│               │   └── item_history.xml (History list item card)
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
![SmartExpenseTracker Output](https://github.com/user-attachments/assets/9087668d-4543-4b82-b9b9-4b90647bfc8b)
