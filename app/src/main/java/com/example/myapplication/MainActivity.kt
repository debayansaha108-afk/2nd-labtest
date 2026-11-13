package com.example.myapplication

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvBalanceAmount: TextView
    private lateinit var tvReminderTime: TextView
    private lateinit var etBalance: EditText
    private lateinit var btnSaveBalance: Button
    private lateinit var btnSetReminder: Button
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        tvBalanceAmount = findViewById(R.id.tv_balance_amount)
        tvReminderTime = findViewById(R.id.tv_reminder_time)
        etBalance = findViewById(R.id.et_balance)
        btnSaveBalance = findViewById(R.id.btn_save_balance)
        btnSetReminder = findViewById(R.id.btn_set_reminder)
        recyclerHistory = findViewById(R.id.recycler_history)

        // Setup RecyclerView
        recyclerHistory.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(emptyList())
        recyclerHistory.adapter = historyAdapter

        // Check notification permission for Android 13+
        checkNotificationPermission()

        // Load data on startup
        refreshDashboard()

        // Load saved reminder time
        loadReminderTimeDisplay()

        // Save Balance & Verify Button Click
        btnSaveBalance.setOnClickListener {
            val amountStr = etBalance.text.toString().trim()
            if (amountStr.isEmpty()) {
                etBalance.error = "Please enter balance amount"
                return@setOnClickListener
            }

            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val calendar = Calendar.getInstance()
            val currentDate = dateFormat.format(calendar.time)
            val currentTime = timeFormat.format(calendar.time)

            val latestSavedBalance = dbHelper.getLatestBalance()

            // If there are previous records, enforce balance verification
            val recordsCount = dbHelper.getAllBalances().size
            if (recordsCount > 0 && latestSavedBalance != "0.00") {
                // Compare entered balance with latest saved balance
                // Normalize formatting check if needed, e.g. parsing as Double
                val enteredVal = amountStr.toDoubleOrNull() ?: 0.0
                val latestVal = latestSavedBalance.toDoubleOrNull() ?: 0.0

                if (enteredVal == latestVal) {
                    // SUCCESSFUL VERIFICATION
                    Toast.makeText(this, "Balance Verified Successfully!", Toast.LENGTH_LONG).show()

                    // Stop alarm sound service
                    stopService(Intent(this, AlarmSoundService::class.java))

                    // Cancel notification
                    cancelActiveNotification()

                    // Save verification record
                    dbHelper.insertVerification(amountStr, "Verified Successfully", currentDate, currentTime)

                    // Save new balance record
                    dbHelper.insertBalance(amountStr, currentDate, currentTime)

                    etBalance.setText("")
                    refreshDashboard()
                    scheduleDailyAlarm()
                } else {
                    // MISMATCH - ALARM KEEPS RINGING
                    Toast.makeText(
                        this,
                        "Balance does not match! Please enter the correct remaining balance (₹$latestSavedBalance).",
                        Toast.LENGTH_LONG
                    ).show()

                    // Log failed verification attempt
                    dbHelper.insertVerification(amountStr, "Mismatch (Failed)", currentDate, currentTime)
                }
            } else {
                // First time entry or 0 balance
                dbHelper.insertBalance(amountStr, currentDate, currentTime)
                dbHelper.insertVerification(amountStr, "Initial Entry", currentDate, currentTime)
                Toast.makeText(this, "Balance saved successfully!", Toast.LENGTH_SHORT).show()
                
                // Stop alarm service if active
                stopService(Intent(this, AlarmSoundService::class.java))
                cancelActiveNotification()

                etBalance.setText("")
                refreshDashboard()
                scheduleDailyAlarm()
            }
        }

        // Set Reminder Time Button Click
        btnSetReminder.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun refreshDashboard() {
        val latest = dbHelper.getLatestBalance()
        tvBalanceAmount.text = "₹ $latest"

        val records = dbHelper.getAllBalances()
        historyAdapter.updateRecords(records)
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun cancelActiveNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ReminderReceiver.NOTIFICATION_ID)
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, hourOfDay, minute ->
            saveReminderTime(hourOfDay, minute)
            scheduleDailyAlarm()
        }, currentHour, currentMinute, false).show()
    }

    private fun saveReminderTime(hour: Int, minute: Int) {
        val sharedPreferences = getSharedPreferences("ExpensePrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putInt("reminder_hour", hour)
            putInt("reminder_minute", minute)
            apply()
        }
        loadReminderTimeDisplay()
    }

    private fun loadReminderTimeDisplay() {
        val sharedPreferences = getSharedPreferences("ExpensePrefs", Context.MODE_PRIVATE)
        val hour = sharedPreferences.getInt("reminder_hour", 20)
        val minute = sharedPreferences.getInt("reminder_minute", 0)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        tvReminderTime.text = "Reminder set for: ${timeFormat.format(calendar.time)}"
    }

    private fun scheduleDailyAlarm() {
        val sharedPreferences = getSharedPreferences("ExpensePrefs", Context.MODE_PRIVATE)
        val hour = sharedPreferences.getInt("reminder_hour", 20)
        val minute = sharedPreferences.getInt("reminder_minute", 0)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Toast.makeText(this, "Daily reminder scheduled successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
