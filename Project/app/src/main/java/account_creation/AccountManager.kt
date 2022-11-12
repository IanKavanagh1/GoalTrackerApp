package account_creation

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import database.TestDatabaseOpenHelper

class AccountManager(context: Context)
{
    private val table_name = "account_details"
    private val columns: Array<String> = arrayOf("ID", "USER_EMAIL", "USER_PASSWORD", "USER_DISPLAY_NAME")
    private val where: String? = "USER_EMAIL = ?"
    private var where_args: Array<String>? = null
    private val group_by: String? = null
    private val having: String? = null
    private val order_by: String? = null

    private var testDatabaseOpenHelper = TestDatabaseOpenHelper( context, "account_details.db", null, 1)
    private lateinit var accountDatabase: SQLiteDatabase


    public fun createAccount(userEmail: String, userPassword: String, userDisplayName: String)
    {
        // Open Writeable connection with Database
        accountDatabase = testDatabaseOpenHelper.writableDatabase

        val newAccount: ContentValues = ContentValues().apply{
            put("USER_EMAIL", userEmail)
            put("USER_PASSWORD", userPassword)
            put("USER_DISPLAY_NAME", userDisplayName)
        }

        accountDatabase.insert("account_details", null, newAccount)
    }

    public fun fetchAccount(userEmail: String, userPassword: String) : Boolean
    {
        // Open Read-Only connection with Database
        accountDatabase = testDatabaseOpenHelper.readableDatabase

        var found = false

        where_args = arrayOf(userEmail)

        var c: Cursor = accountDatabase.query(table_name, columns, where, where_args, group_by, having, order_by)

        var text  = ""
        c.moveToFirst()
        for(i in 0 until c.count)
        {
            found = c.getString(1) == userEmail && c.getString(2) == userPassword
            text += c.getString(1) + " " + c.getString(2)
            Log.d("Account Details Found:", text)
            c.moveToNext()
        }

        return found
    }
}