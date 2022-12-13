package database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import feature_goals.GoalDataModel
import feature_goals.GoalTypes
import shared.Consts

class GoalDatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                             version: Int) : SQLiteOpenHelper(context, name, factory, version)
{
    private val cont = context
    private val tableName = Consts.GOAL_DATABASE

    private val columns: Array<String> = arrayOf("GOAL_ID", "GOAL_TYPE", "GOAL_NAME", "GOAL_TARGET",
                                                    "GOAL_CURRENT", "GOAL_COMPLETED", "USER_ID")
    private val where: String = "USER_ID = ?"
    private var where_args: Array<String>? = null
    private val group_by: String? = null
    private val having: String? = null
    private val order_by: String? = null

    // create table sql query for the goal database
    private val CREATE_TABLE :String = "CREATE TABLE $tableName(" +
            "GOAL_ID integer PRIMARY KEY AUTOINCREMENT," +
            "GOAL_TYPE integer," +
            "GOAL_NAME string," +
            "GOAL_TARGET string," +
            "GOAL_CURRENT string," +
            "GOAL_COMPLETED integer," +
            "USER_ID integer, FOREIGN KEY('USER_ID') REFERENCES user_database(ID)" +
            ")"

    // drop table sql query for the goal database
    private val DROP_TABLE: String = "DROP TABLE IF EXISTS $tableName"

    // create a writable and readable database
    private val wb = this.writableDatabase
    private val rb = this.readableDatabase

    // create database
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    // update database
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_TABLE)
        p0?.execSQL(CREATE_TABLE)
    }

    // returns true if the insert query returns successful, false otherwise
    fun createGoal(goalType: Int, goalName: String, goalTarget: String, goalCurrent: String, userId: Int) : Boolean
    {
        // content values with all the provided data
        val newGoal: ContentValues = ContentValues().apply {
            put("GOAL_TYPE", goalType)
            put("GOAL_NAME", goalName)
            put("GOAL_TARGET", goalTarget)
            put("GOAL_CURRENT", goalCurrent)
            put("GOAL_COMPLETED", 0)
            put("USER_ID", userId)
        }

        // add new data to the database
        // store the result value
        val result = wb.insert(tableName, null, newGoal)

        // check if the insert query was successful
        if(result == -1L)
        {
            // return false if the query failed
            Log.d("Goal Database Helper:", "Insert Data Failed")
            return false
        }

        // return true otherwise
        return true
    }

    // returns list of goals tied to the provided user id
    fun fetchGoals(userId: Int) : ArrayList<GoalDataModel>
    {
        // Create variable to store all the goals
        val userGoals = ArrayList<GoalDataModel>()

        where_args = arrayOf(userId.toString())

        // query the database to get all goals linked to the provided user id
        val c: Cursor = rb.query(tableName, columns, where, where_args, group_by, having, order_by)

        // loop through the data found
        c.moveToFirst()
        for(i in 0 until c.count)
        {
            // add the goals to the userGoals list
            var goalProgress = 0f

            // Use the step counter sensor data to work out progress towards fitness goals
            if(c.getInt(1) == GoalTypes.Fitness.ordinal)
            {
                val sharedPreferences = cont.getSharedPreferences(Consts.USER_PREFS, Context.MODE_PRIVATE)
                val savedStepCount = sharedPreferences.getFloat(Consts.PREFS_PREVIOUS_TOTAL_STEPS, 0f)

                if(checkProgressIsNumeric(c.getString(3)))
                {
                    goalProgress = savedStepCount
                }
            }

            userGoals.add(GoalDataModel(c.getInt(0), c.getInt(1), c.getString(2), goalProgress,
                c.getInt(5)))
            c.moveToNext()
        }

        // close the cursor and return the user goals list
        c.close()
        return  userGoals
    }

    // return true if the update query was successful, false otherwise
    fun updateGoal(goalId: Int, updatedGoalName: String, updatedGoalProgress: String, updatedGoalType: GoalTypes) : Boolean
    {
        // where args for the update query
        val where = "GOAL_ID = ?"
        val whereArgs = arrayOf(goalId.toString())

        // update the goal name for the selected goal
        val updatedGoalValues = ContentValues().apply {
            put("GOAL_NAME", updatedGoalName)
            put("GOAL_CURRENT", updatedGoalProgress)
            put("GOAL_TYPE", updatedGoalType.ordinal)
        }

        // store the result
        val result = wb.update(tableName, updatedGoalValues, where, whereArgs)

        // check if the update query was successful
        if(result == -1)
        {
            // return false if it failed
            Log.d("Goal Database Helper:", "Update Data Failed")
            return false
        }

        // true otherwise
        return true
    }

    // removes the goal with the provided id from the database
    fun removeGoal(goalId: Int)
    {
        val query = "DELETE FROM $tableName WHERE GOAL_ID='$goalId'"
        wb.execSQL(query)
    }

    // Returns true if the update query was successful, false otherwise
    fun markGoalAsCompleted(goalId: Int) : Boolean
    {
        // where args for the update query
        val where = "GOAL_ID = ?"
        val whereArgs = arrayOf(goalId.toString())

        // update the goal name for the selected goal
        val updatedGoalValues = ContentValues().apply {
            put("GOAL_COMPLETED", 1)
        }

        // store the result
        val result = wb.update(tableName, updatedGoalValues, where, whereArgs)

        // check if the update query was successful
        if(result == -1)
        {
            // return false if it failed
            Log.d("Goal Database Helper:", "Update Data Failed")
            return false
        }

        // true otherwise
        return true
    }

    // Safe guard against user entering a non numeric value which can result in a crash
    private fun checkProgressIsNumeric(check : String) : Boolean
    {
        return check.toFloatOrNull() != null
    }
}