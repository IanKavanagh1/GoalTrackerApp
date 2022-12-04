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
    private val where: String = "USER_ID = ?"
    private var where_args: Array<String>? = null
    private val group_by: String? = null
    private val having: String? = null
    private val order_by: String? = null

    private var goalDatabaseOpenHelper = GoalDatabaseOpenHelper(context, "goals_test.db", null, 1)
    private lateinit var goalDatabase: SQLiteDatabase

    fun createGoal(goalType: Int, goalName: String, goalTarget: String, userId: Int)
    {
        // Open Writeable connection with Database
        goalDatabase = goalDatabaseOpenHelper.writableDatabase

        val newGoal: ContentValues = ContentValues().apply {
            put("GOAL_TYPE", goalType)
            put("GOAL_NAME", goalName)
            put("GOAL_TARGET", goalTarget)
            put("USER_ID", userId)
        }

        goalDatabase.insert(table_name, null, newGoal)
        goalDatabase.close()
    }

    fun fetchGoals(userId: Int) : ArrayList<GoalDataModel>
    {
        // Create variable to store all the goals
        val userGoals = ArrayList<GoalDataModel>()

        // Open Read-Only connection with Database
        goalDatabase = goalDatabaseOpenHelper.readableDatabase

        where_args = arrayOf(userId.toString())

        val c: Cursor = goalDatabase.query(table_name, columns, where, where_args, group_by, having, order_by)

        c.moveToFirst()
        for(i in 0 until c.count)
        {
            userGoals.add(GoalDataModel(c.getInt(0), 0, 0, c.getString(2),0f))
            c.moveToNext()
        }

        c.close()
        goalDatabase.close()

        return  userGoals
    }

    fun updateGoal(goalId: Int, updatedGoalName: String)
    {
        Log.d("Goal Manager"," Update Goal ID $goalId")
        Log.d("Goal Manager", "Updated Goal Name $updatedGoalName")
        goalDatabase = goalDatabaseOpenHelper.writableDatabase

        val where = "GOAL_ID = ?"
        val where_args = arrayOf(goalId.toString())

        val updatedGoalValues = ContentValues().apply {
            put("GOAL_TYPE", 0)
            put("GOAL_NAME", updatedGoalName)
            put("GOAL_TARGET", "Test")
            put("USER_ID", 6)
        }

        goalDatabase.update(table_name, updatedGoalValues, where, where_args)

        goalDatabase.close()
    }

    fun removeGoal(goalId: Int)
    {
        goalDatabase = goalDatabaseOpenHelper.writableDatabase

        val query = "DELETE FROM $table_name WHERE GOAL_ID='$goalId'"

        goalDatabase.execSQL(query)

        goalDatabase.close()
    }
}