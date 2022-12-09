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

    // create writable and readable database
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

    // Returns true if the insert query was successful, false otherwise
    fun insertData(userEmail : String, userPassword: String, userDisplayName: String) : Boolean
    {
        // content values with all provided parameters for the new user
        val newAccount: ContentValues = ContentValues().apply{
            put("USER_EMAIL", userEmail)
            put("USER_PASSWORD", userPassword)
            put("USER_DISPLAY_NAME", userDisplayName)
        }

        // add data to database
        // store result
        val result = wb.insert(tableName, null, newAccount)

        // check if the insert was successful
        if(result == -1L)
        {
            // return false if it was not
            Log.d("Account Database Helper:", "Insert Data Failed")
            return false
        }

        // return true if it was
        return true
    }

    // Returns true if the userEmail exists
    fun checkUserName(userEmail: String) : Boolean
    {
        // query the database to find the provided user email
        val userEmails = arrayOf(userEmail)
        val cursor = rb.rawQuery("SELECT * FROM $tableName WHERE USER_EMAIL = ?", userEmails)

        // if the data count is more than 0 we have found an email that matches the provided email
        if(cursor.count > 0)
        {
            // close the cursor and return true
            cursor.close()
            return true
        }

        // otherwise we can return false
        cursor.close()
        return false
    }

    // returns true if the email and password are correct
    fun checkUserNameAndPassword(userEmail: String, userPassword: String) : Boolean
    {
        // check if the email and password are correct
        val userEmailsAndPasswords = arrayOf(userEmail, userPassword)
        val cursor = rb.rawQuery("SELECT * FROM $tableName WHERE USER_EMAIL = ? AND USER_PASSWORD = ?", userEmailsAndPasswords)

        if(cursor.count > 0)
        {
            // close the cursor and return true if they are
            cursor.close()
            return true
        }

        // otherwise close the cursor and return false
        cursor.close()
        return false
    }

    // returns the userId if found, -1 if not
    fun getUserId(userEmail: String, userPassword: String) : Int
    {
        // query the database to try get the user id
        val userEmailsAndPasswords = arrayOf(userEmail, userPassword)
        val cursor = rb.rawQuery("SELECT * FROM $tableName WHERE USER_EMAIL = ? AND USER_PASSWORD = ?", userEmailsAndPasswords)

        // loop through the data
        cursor.moveToFirst()
        for(i in 0 until cursor.count)
        {
            // get the user Id
            val userId = cursor.getInt(0)

            // close the cursor and return the userId
            cursor.close()
            return userId
        }

        // close the cursor and return -1 as no userId was found
        cursor.close()
        return -1
    }

    // returns the user display name if found, an empty string if not
    fun getUserDisplayName(userId: Int) : String
    {
        // query the database
        val id = arrayOf(userId.toString())
        val cursor = rb.rawQuery("SELECT * FROM $tableName WHERE ID = ? ", id)

        // loop through the found data
        cursor.moveToFirst()
        for(i in 0 until cursor.count)
        {
            // grab the user display name
            val userDisplayName = cursor.getString(3)

            // close the cursor and return the user display name
            cursor.close()
            return userDisplayName
        }

        // otherwise return an empty string
        cursor.close()
        return ""
    }

    // Returns a list of strings, an empty list if no user was found
    fun getUserEmailDisplayNameAndPassword(userId : Int) : Array<String>
    {
        // query the database to get the email and password for the provided userId
        val id = arrayOf(userId.toString())
        val cursor = rb.rawQuery("SELECT * FROM $tableName WHERE ID = ? ", id)

        // loop through data
        cursor.moveToFirst()
        for(i in 0 until cursor.count)
        {
            // get the user email
            val userEmail = cursor.getString(1)

            val userPassword = cursor.getString(2)

            // get the user display name
            val userDisplayName = cursor.getString(3)

            // close the cursor and return the list
            cursor.close()
            return arrayOf(userEmail, userPassword, userDisplayName)
        }

        // close the cursor and return an empty list as not user was found
        cursor.close()
        return arrayOf()
    }
}