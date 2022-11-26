package feature_goals

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import database.GoalDatabaseOpenHelper

class GoalManager (context: Context)
{
    private val table_name = "goals_test"
    private val columns: Array<String> = arrayOf("GOAL_ID", "GOAL_TYPE", "GOAL_NAME", "GOAL_TARGET", "USER_ID")
    private val where: String? = "USER_ID = ?"
    private var where_args: Array<String>? = null
    private val group_by: String? = null
    private val having: String? = null
    private val order_by: String? = null

    private var goalDatabaseOpenHelper = GoalDatabaseOpenHelper(context, "goals_test.db", null, 1)
    private lateinit var goalDatabase: SQLiteDatabase

    public fun createGoal(goalType: Int, goalName: String, goalTarget: String, userId: Int)
    {
        // Open Writeable connection with Database
        goalDatabase = goalDatabaseOpenHelper.writableDatabase

        var newGoal: ContentValues = ContentValues().apply {
            put("GOAL_TYPE", goalType)
            put("GOAL_NAME", goalName)
            put("GOAL_TARGET", goalTarget)
            put("USER_ID", userId)
        }

        goalDatabase.insert(table_name, null, newGoal)
    }

    public fun fetchGoals(userId: Int) : ArrayList<GoalDataModel>
    {
        // Create variable to store all the goals
        var userGoals = ArrayList<GoalDataModel>()

        // Open Read-Only connection with Database
        goalDatabase = goalDatabaseOpenHelper.readableDatabase

        where_args = arrayOf(userId.toString())

        var c: Cursor = goalDatabase.query(table_name, columns, where, where_args, group_by, having, order_by)

        c.moveToFirst()
        for(i in 0 until c.count)
        {
            userGoals.add(GoalDataModel(0, 0, c.getString(2),0f))
            c.moveToNext()
        }

        return  userGoals
    }

    public fun updateGoal()
    {
        Log.d("Goal Manager"," Update Goal - To Be Implemented")
    }

    public fun removeGoal()
    {
        Log.d("Goal Manager","Remove Goal - To Be Implemented")
    }
}