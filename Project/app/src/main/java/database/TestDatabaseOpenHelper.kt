package database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TestDatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                             version: Int) : SQLiteOpenHelper(context, name, factory, version)
{
    private val CREATE_TABLE :String = "create table account_details(" +
            "ID integer primary key autoincrement,"+
            "USER_EMAIL string," +
            "USER_PASSWORD string," +
            "USER_DISPLAY_NAME string" +
            ")"

    private val DROP_TABLE: String = "drop table account_details"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_TABLE)
        p0?.execSQL(CREATE_TABLE)
    }
}