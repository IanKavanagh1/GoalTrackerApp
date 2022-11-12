package database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GoalDatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                             version: Int) : SQLiteOpenHelper(context, name, factory, version)
{
    //TODO: Review database design as a whole
    private val CREATE_TABLE :String = "create table user_goals(" +
            "ID integer primary key autoincrement,"+
            "GOAL_NAME string," +
            "GOAL_TARGET string," +
            "GOAL_CURRENT string" +
            ")"

    private val DROP_TABLE: String = "drop table user_goals"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_TABLE)
        p0?.execSQL(CREATE_TABLE)
    }
}