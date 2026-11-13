package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class BalanceRecord(
    val id: Int,
    val amount: String,
    val date: String,
    val time: String
)

data class VerificationRecord(
    val id: Int,
    val verifiedBalance: String,
    val status: String,
    val date: String,
    val time: String
)

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "SmartExpenseTracker.db"
        private const val DATABASE_VERSION = 2

        private const val TABLE_BALANCE = "balance_table"
        private const val COL_B_ID = "id"
        private const val COL_B_AMOUNT = "amount"
        private const val COL_B_DATE = "date"
        private const val COL_B_TIME = "time"

        private const val TABLE_VERIFICATION = "verification_table"
        private const val COL_V_ID = "id"
        private const val COL_V_BALANCE = "verified_balance"
        private const val COL_V_STATUS = "status"
        private const val COL_V_DATE = "date"
        private const val COL_V_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createBalanceTable = ("CREATE TABLE $TABLE_BALANCE (" +
                "$COL_B_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_B_AMOUNT TEXT, " +
                "$COL_B_DATE TEXT, " +
                "$COL_B_TIME TEXT)")
        db?.execSQL(createBalanceTable)

        val createVerificationTable = ("CREATE TABLE $TABLE_VERIFICATION (" +
                "$COL_V_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_V_BALANCE TEXT, " +
                "$COL_V_STATUS TEXT, " +
                "$COL_V_DATE TEXT, " +
                "$COL_V_TIME TEXT)")
        db?.execSQL(createVerificationTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BALANCE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_VERIFICATION")
        onCreate(db)
    }

    fun insertBalance(amount: String, date: String, time: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_B_AMOUNT, amount)
            put(COL_B_DATE, date)
            put(COL_B_TIME, time)
        }
        val result = db.insert(TABLE_BALANCE, null, values)
        db.close()
        return result != -1L
    }

    fun getAllBalances(): List<BalanceRecord> {
        val list = mutableListOf<BalanceRecord>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_BALANCE ORDER BY $COL_B_ID DESC", null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(COL_B_ID)
            val amountIndex = cursor.getColumnIndex(COL_B_AMOUNT)
            val dateIndex = cursor.getColumnIndex(COL_B_DATE)
            val timeIndex = cursor.getColumnIndex(COL_B_TIME)

            do {
                val id = if (idIndex != -1) cursor.getInt(idIndex) else 0
                val amount = if (amountIndex != -1) cursor.getString(amountIndex) else "0"
                val date = if (dateIndex != -1) cursor.getString(dateIndex) else ""
                val time = if (timeIndex != -1) cursor.getString(timeIndex) else ""
                list.add(BalanceRecord(id, amount, date, time))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun getLatestBalance(): String {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT $COL_B_AMOUNT FROM $TABLE_BALANCE ORDER BY $COL_B_ID DESC LIMIT 1", null)
        var latest = "0.00"
        if (cursor.moveToFirst()) {
            val amountIndex = cursor.getColumnIndex(COL_B_AMOUNT)
            if (amountIndex != -1) {
                latest = cursor.getString(amountIndex) ?: "0.00"
            }
        }
        cursor.close()
        db.close()
        return latest
    }

    fun insertVerification(balance: String, status: String, date: String, time: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_V_BALANCE, balance)
            put(COL_V_STATUS, status)
            put(COL_V_DATE, date)
            put(COL_V_TIME, time)
        }
        val result = db.insert(TABLE_VERIFICATION, null, values)
        db.close()
        return result != -1L
    }

    fun getAllVerifications(): List<VerificationRecord> {
        val list = mutableListOf<VerificationRecord>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_VERIFICATION ORDER BY $COL_V_ID DESC", null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(COL_V_ID)
            val balanceIndex = cursor.getColumnIndex(COL_V_BALANCE)
            val statusIndex = cursor.getColumnIndex(COL_V_STATUS)
            val dateIndex = cursor.getColumnIndex(COL_V_DATE)
            val timeIndex = cursor.getColumnIndex(COL_V_TIME)

            do {
                val id = if (idIndex != -1) cursor.getInt(idIndex) else 0
                val balance = if (balanceIndex != -1) cursor.getString(balanceIndex) else "0"
                val status = if (statusIndex != -1) cursor.getString(statusIndex) else ""
                val date = if (dateIndex != -1) cursor.getString(dateIndex) else ""
                val time = if (timeIndex != -1) cursor.getString(timeIndex) else ""
                list.add(VerificationRecord(id, balance, status, date, time))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }
}
