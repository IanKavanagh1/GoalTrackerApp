package goal_creation

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import database.GoalDatabaseOpenHelper

class GoalManager (context: Context)
{
    private val table_name = "user_goals"
    private val columns: Array<String> = arrayOf("ID", "GOAL_NAME", "GOAL_TARGET", "GOAL_CURRENT")
    private val where: String? = "GOAL_NAME = ?"
    private var where_args: Array<String>? = null
    private val group_by: String? = null
    private val having: String? = null
    private val order_by: String? = null

    private var goalDatabaseOpenHelper = GoalDatabaseOpenHelper(context, "user_goals.db", null, 1)
    private lateinit var goalDatabase: SQLiteDatabase

    public fun createGoal(goalName: String, goalTarget: String, goalCurrent: String)
    {
        // Open Writeable connection with Database
        goalDatabase = goalDatabaseOpenHelper.writableDatabase

        var newGoal: ContentValues = ContentValues().apply {
            put("GOAL_NAME", goalName)
            put("GOAL_TARGET", goalTarget)
            put("GOAL_CURRENT", goalCurrent)
        }

        goalDatabase.insert(table_name, null, newGoal)
    }

    public fun fetchGoals(goalName: String)
    {
        // Open Read-Only connection with Database
        goalDatabase = goalDatabaseOpenHelper.readableDatabase

        where_args = arrayOf(goalName)

        var c: Cursor = goalDatabase.query(table_name, columns, where, where_args, group_by, having, order_by)

        var text = ""

        c.moveToFirst()
        for(i in 0 until c.count)
        {
            text += c.getInt(0).toString() + " " + c.getString(1) + " " + c.getString(2) + " " +
                    c.getString(3) + "\n"
            Log.d("Goal Manager: Goal Data:", text)
            c.moveToNext()
        }
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