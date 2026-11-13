# Upgrade SmartExpenseTracker to Daily Alarm & Balance Verification System

Implement a robust daily alarm system with continuous alarm sound playback (via Foreground Service), SQLite balance verification against the latest saved balance, verification records history, and boot restoration.

## User Review Required

> [!IMPORTANT]
> This upgrade adds:
> 1. **`AlarmSoundService.kt`**: A Foreground Service playing continuous alarm sound via `MediaPlayer` until stopped.
> 2. **`BootReceiver.kt`**: Reschedules the daily alarm after device reboot (`BOOT_COMPLETED`).
> 3. **Balance Verification Logic in `MainActivity.kt`**: When saving balance while an alarm is active, compares the entered amount with the latest saved balance. If it matches, stops the alarm, cancels notification, logs a verification record in SQLite, and schedules the next alarm. If it doesn't match, keeps the alarm ringing.
> 4. **Enhanced `DatabaseHelper.kt`**: Adds support for verification records table (`verification_table`).

## Proposed Changes

### 1. Database Layer
- **`DatabaseHelper.kt`**:
  - Keep `balance_table`.
  - Add `verification_table` (`id`, `verified_balance`, `status`, `date`, `time`).
  - Add methods for inserting and retrieving verification records.

### 2. Audio & Background Services
- **`AlarmSoundService.kt`**:
  - Foreground Service using `MediaPlayer` (system default alarm sound or ringtone) looping continuously.
  - Stopped via intent when verification succeeds.
- **`ReminderReceiver.kt`**:
  - Triggered by AlarmManager.
  - Starts `AlarmSoundService` and shows high-priority notification with intent to open `MainActivity`.
- **`BootReceiver.kt`**:
  - Listens to `ACTION_BOOT_COMPLETED` to reschedule the daily alarm using saved time in SharedPreferences.

### 3. UI & Verification Logic
- **`MainActivity.kt`**:
  - Update "Save Balance" button to check if an alarm/alarm service is active.
  - Perform verification: compare `et_balance.text.toString().trim()` with `dbHelper.getLatestBalance()`.
  - If match: stop `AlarmSoundService`, cancel notification, insert verification record (Status: "Verified Successfully"), save new balance record, schedule next alarm.
  - If mismatch: show error toast/message ("Balance does not match! Please enter the correct remaining balance."), keep alarm ringing.
  - Display verification history alongside balance history.

### 4. Manifest & Permissions
- **`AndroidManifest.xml`**:
  - Add permissions: `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_MEDIA_PLAYBACK`, `RECEIVE_BOOT_COMPLETED`, `WAKE_LOCK`, `POST_NOTIFICATIONS`.
  - Register `AlarmSoundService` and `BootReceiver`.

## Verification Plan

### Automated Tests
- Build project (`gradlew assembleDebug`) to verify clean compilation.

### Manual Verification
- Set reminder time.
- Trigger alarm (or wait for scheduled time / test via intent).
- Verify continuous alarm sound and notification.
- Test entering incorrect balance (alarm continues).
- Test entering correct balance (alarm stops, notification clears, verification logged, next alarm scheduled).