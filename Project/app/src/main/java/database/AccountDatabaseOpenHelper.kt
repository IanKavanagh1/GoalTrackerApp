package database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import shared.Consts

class AccountDatabaseOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?,
                             version: Int) : SQLiteOpenHelper(context, name, factory, version)
{
    private val tableName = Consts.USER_DATABASE
    private val CREATE_TABLE :String = "CREATE TABLE $tableName(" +
            "ID integer PRIMARY KEY AUTOINCREMENT,"+
            "USER_EMAIL string," +
            "USER_PASSWORD string," +
            "USER_DISPLAY_NAME string" +
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

    fun insertData(userEmail : String, userPassword: String, userDisplayName: String) : Boolean
    {
        val newAccount: ContentValues = ContentValues().apply{
            put("USER_EMAIL", userEmail)
            put("USER_PASSWORD", userPassword)
            put("USER_DISPLAY_NAME", userDisplayName)
        }

        val result = wb.insert(tableName, null, newAccount)

        if(result == -1L)
        {
            Log.d("Account Database Helper:", "Insert Data Failed")
            return false
        }

        return true
    }

    fun checkUserName(userEmail: String) : Boolean
    {
        val userEmails = arrayOf(userEmail)
        val cursor = rb.rawQuery("SELECT * FROM $tableName WHERE USER_EMAIL = ?", userEmails)

        if(cursor.count > 0)
        {
            cursor.close()
            return true
        }

        return false
    }

    fun checkUserNameAndPassword(userEmail: String, userPassword: String) : Boolean
    {
        val userEmailsAndPasswords = arrayOf(userEmail, userPassword)
        val cursor = rb.rawQuery("SELECT * FROM $tableName WHERE USER_EMAIL = ? AND USER_PASSWORD = ?", userEmailsAndPasswords)

        if(cursor.count > 0)
        {
            cursor.close()
            return true
        }
        return false
    }

    fun getUserId(userEmail: String, userPassword: String) : Int
    {
        val userEmailsAndPasswords = arrayOf(userEmail, userPassword)
        val cursor = rb.rawQuery("SELECT * FROM $tableName WHERE USER_EMAIL = ? AND USER_PASSWORD = ?", userEmailsAndPasswords)

        cursor.moveToFirst()
        for(i in 0 until cursor.count)
        {
            val userId = cursor.getInt(0)
            cursor.close()
            return userId
        }
        return -1
    }

    fun getUserDisplayName(userId: Int) : String
    {
        val id = arrayOf(userId.toString())
        val cursor = rb.rawQuery("SELECT * FROM $tableName WHERE ID = ? ", id)

        cursor.moveToFirst()
        for(i in 0 until cursor.count)
        {
            val userDisplayName = cursor.getString(3)
            cursor.close()
            return userDisplayName
        }
        return ""
    }

    fun getUserEmailAndDisplayName(userId : Int) : Array<String>
    {
        val id = arrayOf(userId.toString())
        val cursor = rb.rawQuery("SELECT * FROM $tableName WHERE ID = ? ", id)

        cursor.moveToFirst()
        for(i in 0 until cursor.count)
        {
            val userEmail = cursor.getString(1)
            val userDisplayName = cursor.getString(3)

            cursor.close()
            return arrayOf(userEmail, userDisplayName)
        }
        return arrayOf()
    }
}