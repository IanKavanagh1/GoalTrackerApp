package database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class HeartRateDatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                                version: Int) : SQLiteOpenHelper(context, name, factory, version)
{
    // TODO: Use database name from Const File

    private val CREATE_TABLE :String = "CREATE TABLE heart_rate_test(" +
            "ID integer PRIMARY KEY AUTOINCREMENT," +
            "HEART_RATE integer," +
            "USER_ID integer, FOREIGN KEY('USER_ID') REFERENCES account_details(ID)" +
            ")"

    private val DROP_TABLE: String = "DROP TABLE IF EXISTS heart_rate_test"

    private val wb = this.writableDatabase
    private val rb = this.readableDatabase

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_TABLE)
        p0?.execSQL(CREATE_TABLE)
    }

    public fun insertData(heartRate: Int, userId: Int) : Boolean
    {
        val newHeartRateEntry: ContentValues = ContentValues().apply{
            put("HEART_RATE", heartRate)
            put("USER_ID", userId)
        }

        //TODO: Replace name with const from Const file
        var result = wb.insert("heart_rate_test", null, newHeartRateEntry)

        if(result == -1L)
        {
            Log.d("Heart Rate Database Helper:", "Insert Data Failed")
            return false
        }

        return true
    }

    public fun getUserHeartRateData(userId: Int) : ArrayList<Int>
    {
        var userID = arrayOf(userId.toString())
        var cursor = rb.rawQuery("SELECT * FROM heart_rate_test WHERE USER_ID = ? ", userID)

        val userHeartRateData = ArrayList<Int>()

        cursor.moveToFirst()
        for(i in 0 until cursor.count)
        {
            Log.d("Heart Rate Database","User Id for Logged In User Is ${cursor.getInt(2)}")
            userHeartRateData.add(cursor.getInt(1))
            cursor.moveToNext()
        }
        return userHeartRateData
    }
}