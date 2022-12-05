package database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import exercise_planner.ExerciseDataModel
import shared.Consts

class ExerciseDatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                                  version: Int) : SQLiteOpenHelper(context, name, factory, version)
{
    private val dbName = Consts.EXERCISE_DATABASE
    // Create table sql query for exercise database
    private val CREATE_TABLE :String = "CREATE TABLE $dbName(" +
            "ID integer PRIMARY KEY AUTOINCREMENT," +
            "START_LAT double," +
            "START_LON double," +
            "END_LAT double," +
            "END_LON double," +
            "DISTANCE_COVERED double," +
            "TIME_TAKEN long," +
            "USER_ID integer, FOREIGN KEY('USER_ID') REFERENCES user_database(ID)" +
            ")"

    // Drop table sql query for exercise database
    private val DROP_TABLE: String = "DROP TABLE IF EXISTS $dbName"

    // create a writeable and readable database
    private val wb = this.writableDatabase
    private val rb = this.readableDatabase

    // create database
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    // upgrade database
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_TABLE)
        p0?.execSQL(CREATE_TABLE)
    }

    // Returns true if the data was added successfully
    fun insertData(startLat: Double,startLon: Double, endLat: Double, endLon: Double, distanceCovered: Double, timeTake: Long, userId: Int) : Boolean
    {
        // content values with all of provided parameters
        val newExerciseEntry: ContentValues = ContentValues().apply{
            put("START_LAT", startLat)
            put("START_LON", startLon)
            put("END_LAT", endLat)
            put("END_LON", endLon)
            put("DISTANCE_COVERED", distanceCovered)
            put("TIME_TAKEN", timeTake)
            put("USER_ID", userId)
        }

        // add new data to the database
        // store the result of the insert query
        val result = wb.insert(dbName, null, newExerciseEntry)

        // check the result
        if(result == -1L)
        {
            // Adding data failed so we return false
            Log.d("Exercise Database Helper:", "Insert Data Failed")
            return false
        }

        // otherwise we can return true
        return true
    }

    // Returns a list of Exercise data for the provided user id
    fun getUserExerciseData(userId: Int) : ArrayList<ExerciseDataModel>
    {
        // query the database to get all data linked to the user id provided
        val userID = arrayOf(userId.toString())
        val cursor = rb.rawQuery("SELECT * FROM $dbName WHERE USER_ID = ? ", userID)

        // store the exercise data
        val userExerciseData = ArrayList<ExerciseDataModel>()

        // loop through the data found in the data base
        cursor.moveToFirst()
        for(i in 0 until cursor.count)
        {
            // add new data to the list for every piece of data found in the database
            // linked to the user id provided
            userExerciseData.add(ExerciseDataModel(cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3),
                cursor.getDouble(4), cursor.getDouble(5), cursor.getLong(6)))
            cursor.moveToNext()
        }

        // close the cursor object and return the list of exercise data
        cursor.close()
        return userExerciseData
    }
}