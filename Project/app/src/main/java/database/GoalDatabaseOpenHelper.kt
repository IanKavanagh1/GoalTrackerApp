package database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import feature_goals.GoalDataModel
import shared.Consts

class GoalDatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                             version: Int) : SQLiteOpenHelper(context, name, factory, version)
{
    private val tableName = Consts.GOAL_DATABASE

    private val columns: Array<String> = arrayOf("GOAL_ID", "GOAL_TYPE", "GOAL_NAME", "GOAL_TARGET",
                                                    "GOAL_CURRENT", "USER_ID")
    private val where: String = "USER_ID = ?"
    private var where_args: Array<String>? = null
    private val group_by: String? = null
    private val having: String? = null
    private val order_by: String? = null

    private val CREATE_TABLE :String = "CREATE TABLE $tableName(" +
            "GOAL_ID integer PRIMARY KEY AUTOINCREMENT," +
            "GOAL_TYPE integer," +
            "GOAL_NAME string," +
            "GOAL_TARGET string," +
            "GOAL_CURRENT string," +
            "USER_ID integer, FOREIGN KEY('USER_ID') REFERENCES user_database(ID)" +
            ")"

    private val DROP_TABLE: String = "DROP TABLE IF EXISTS $tableName"

    private val wb = this.writableDatabase
    private val rb = this.readableDatabase

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_TABLE)
        p0?.execSQL(CREATE_TABLE)
    }

    fun createGoal(goalType: Int, goalName: String, goalTarget: String, goalCurrent: String, userId: Int) : Boolean
    {
        val newGoal: ContentValues = ContentValues().apply {
            put("GOAL_TYPE", goalType)
            put("GOAL_NAME", goalName)
            put("GOAL_TARGET", goalTarget)
            put("GOAL_CURRENT", goalCurrent)
            put("USER_ID", userId)
        }

        val result = wb.insert(tableName, null, newGoal)

        if(result == -1L)
        {
            Log.d("Goal Database Helper:", "Insert Data Failed")
            return false
        }

        return true
    }

    fun fetchGoals(userId: Int) : ArrayList<GoalDataModel>
    {
        // Create variable to store all the goals
        val userGoals = ArrayList<GoalDataModel>()

        where_args = arrayOf(userId.toString())

        val c: Cursor = rb.query(tableName, columns, where, where_args, group_by, having, order_by)

        c.moveToFirst()
        for(i in 0 until c.count)
        {
            userGoals.add(GoalDataModel(c.getInt(0), 0, 0, c.getString(2),0f))
            c.moveToNext()
        }

        c.close()

        return  userGoals
    }

    fun updateGoal(goalId: Int, updatedGoalName: String) : Boolean
    {
        val where = "GOAL_ID = ?"
        val whereArgs = arrayOf(goalId.toString())

        val updatedGoalValues = ContentValues().apply {
            put("GOAL_NAME", updatedGoalName)
        }

        val result = wb.update(tableName, updatedGoalValues, where, whereArgs)

        if(result == -1)
        {
            Log.d("Goal Database Helper:", "Update Data Failed")
            return false
        }

        return true
    }

    fun removeGoal(goalId: Int)
    {
        val query = "DELETE FROM $tableName WHERE GOAL_ID='$goalId'"

        val result = wb.execSQL(query)
    }
}