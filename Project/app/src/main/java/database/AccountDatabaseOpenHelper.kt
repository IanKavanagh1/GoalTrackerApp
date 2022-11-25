package database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class AccountDatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                             version: Int) : SQLiteOpenHelper(context, name, factory, version)
{
    private val CREATE_TABLE :String = "CREATE TABLE users_test(" +
            "ID integer PRIMARY KEY AUTOINCREMENT,"+
            "USER_EMAIL string," +
            "USER_PASSWORD string," +
            "USER_DISPLAY_NAME string" +
            ")"

    private val DROP_TABLE: String = "DROP TABLE IF EXISTS users_test"

    private val wb = this.writableDatabase
    private val rb = this.readableDatabase

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_TABLE)
        p0?.execSQL(CREATE_TABLE)
    }

    public fun insertData(userEmail : String, userPassword: String, userDisplayName: String) : Boolean
    {
        val newAccount: ContentValues = ContentValues().apply{
            put("USER_EMAIL", userEmail)
            put("USER_PASSWORD", userPassword)
            put("USER_DISPLAY_NAME", userDisplayName)
        }

        var result = wb.insert("users_test", null, newAccount)

        if(result == -1L)
        {
            Log.d("Account Database Helper:", "Insert Data Failed")
            return false
        }

        return true
    }

    public fun checkUserName(userEmail: String) : Boolean
    {
        var userEmails = arrayOf(userEmail)
        var cursor = rb.rawQuery("SELECT * FROM users_test WHERE USER_EMAIL = ?", userEmails)

        if(cursor.count > 0)
        {
            return true
        }

        return false
    }

    public fun checkUserNameAndPassword(userEmail: String, userPassword: String) : Boolean
    {
        var userEmailsAndPasswords = arrayOf(userEmail, userPassword)
        var cursor = rb.rawQuery("SELECT * FROM users_test WHERE USER_EMAIL = ? AND USER_PASSWORD = ?", userEmailsAndPasswords)

        if(cursor.count > 0)
        {
            return true
        }
        return false
    }

    public fun getUserId(userEmail: String, userPassword: String) : Int
    {
        var userEmailsAndPasswords = arrayOf(userEmail, userPassword)
        var cursor = rb.rawQuery("SELECT * FROM users_test WHERE USER_EMAIL = ? AND USER_PASSWORD = ?", userEmailsAndPasswords)

        cursor.moveToFirst()
        for(i in 0 until cursor.count)
        {
            Log.d("AccountDatabase","User Id for Logged In User Is ${cursor.getInt(0)}")
            return cursor.getInt(0)
        }
        return -1
    }
}