package database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//TODO: Update file to follow update pattern like Accounts and other databases used in the app
class GoalDatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                             version: Int) : SQLiteOpenHelper(context, name, factory, version)
{
    private val CREATE_TABLE :String = "CREATE TABLE goals_test(" +
            "GOAL_ID integer PRIMARY KEY AUTOINCREMENT,"+
            "GOAL_TYPE integer," +
            "GOAL_NAME string," +
            "GOAL_TARGET string," +
            "USER_ID integer, FOREIGN KEY('USER_ID') REFERENCES account_details(ID)" +
            ")"

    private val DROP_TABLE: String = "DROP TABLE goals_test"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_TABLE)
        p0?.execSQL(CREATE_TABLE)
    }
}