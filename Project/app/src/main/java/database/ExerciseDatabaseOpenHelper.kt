package database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import exercise_planner.ExerciseDataModel

class ExerciseDatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                                  version: Int) : SQLiteOpenHelper(context, name, factory, version)
{
    // TODO: Use database name from Const File

    private val CREATE_TABLE :String = "CREATE TABLE exercise_database_test(" +
            "ID integer PRIMARY KEY AUTOINCREMENT," +
            "START_LAT double," +
            "START_LON double," +
            "END_LAT double," +
            "END_LON double," +
            "DISTANCE_COVERED double," +
            "TIME_TAKEN long," +
            "USER_ID integer, FOREIGN KEY('USER_ID') REFERENCES account_details(ID)" +
            ")"

    private val DROP_TABLE: String = "DROP TABLE IF EXISTS exercise_database_test"

    private val wb = this.writableDatabase
    private val rb = this.readableDatabase

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_TABLE)
        p0?.execSQL(CREATE_TABLE)
    }

    public fun insertData(startLat: Double,startLon: Double, endLat: Double, endLon: Double, distanceCovered: Double, timeTake: Long, userId: Int) : Boolean
    {
        val newExerciseEntry: ContentValues = ContentValues().apply{
            put("START_LAT", startLat)
            put("START_LON", startLon)
            put("END_LAT", endLat)
            put("END_LON", endLon)
            put("DISTANCE_COVERED", distanceCovered)
            put("TIME_TAKEN", timeTake)
            put("USER_ID", userId)
        }

        //TODO: Replace name with const from Const file
        var result = wb.insert("exercise_database_test", null, newExerciseEntry)

        if(result == -1L)
        {
            Log.d("Exercise Database Helper:", "Insert Data Failed")
            return false
        }

        return true
    }

    public fun getUserExerciseData(userId: Int) : ArrayList<ExerciseDataModel>
    {
        var userID = arrayOf(userId.toString())
        var cursor = rb.rawQuery("SELECT * FROM heart_rate_test WHERE USER_ID = ? ", userID)

        val userExerciseData = ArrayList<ExerciseDataModel>()

        cursor.moveToFirst()
        for(i in 0 until cursor.count)
        {
            userExerciseData.add(ExerciseDataModel(cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3),
                cursor.getDouble(4), cursor.getDouble(5), cursor.getLong(6)))
            cursor.moveToNext()
        }
        return userExerciseData
    }
}