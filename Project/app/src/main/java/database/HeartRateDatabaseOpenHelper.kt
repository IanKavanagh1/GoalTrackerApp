package database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import shared.Consts

class HeartRateDatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                                version: Int) : SQLiteOpenHelper(context, name, factory, version)
{
    private val dbName = Consts.HEART_RATE_DATABASE

    private val CREATE_TABLE :String = "CREATE TABLE $dbName(" +
            "ID integer PRIMARY KEY AUTOINCREMENT," +
            "HEART_RATE integer," +
            "USER_ID integer, FOREIGN KEY('USER_ID') REFERENCES user_database(ID)" +
            ")"

    private val DROP_TABLE: String = "DROP TABLE IF EXISTS $dbName"

    // create writable and readable database
    private val wb = this.writableDatabase
    private val rb = this.readableDatabase

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_TABLE)
        p0?.execSQL(CREATE_TABLE)
    }

    // Returns true if the insert query was successful
    fun insertData(heartRate: Int, userId: Int) : Boolean
    {
        // content values for the database entry
        val newHeartRateEntry: ContentValues = ContentValues().apply{
            put("HEART_RATE", heartRate)
            put("USER_ID", userId)
        }

        // add new data and store the result
        val result = wb.insert(dbName, null, newHeartRateEntry)

        // check if the insert query
        if(result == -1L)
        {
            // insert data failed return false
            Log.d("Heart Rate Database Helper:", "Insert Data Failed")
            return false
        }

        // insert data successful, return true
        return true
    }

    // Returns a list of all user heart rate data
    fun getUserHeartRateData(userId: Int) : ArrayList<Int>
    {
        // query the database to get all heart rate data linked to the provided userId
        val userID = arrayOf(userId.toString())
        val cursor = rb.rawQuery("SELECT * FROM $dbName WHERE USER_ID = ? ", userID)

        // store data
        val userHeartRateData = ArrayList<Int>()

        // loop through data found
        cursor.moveToFirst()
        for(i in 0 until cursor.count)
        {
            // Add all data to list
            Log.d("Heart Rate Database","User Id for Logged In User Is ${cursor.getInt(2)}")
            userHeartRateData.add(cursor.getInt(1))
            cursor.moveToNext()
        }

        // close cursor and return list of heart rate data
        cursor.close()
        return userHeartRateData
    }
}