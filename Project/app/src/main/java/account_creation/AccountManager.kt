package account_creation

import android.content.Context
import database.AccountDatabaseOpenHelper

object AccountManager
{
    private lateinit var testDatabaseOpenHelper: AccountDatabaseOpenHelper

    public fun setUpDatabase(context: Context)
    {
        testDatabaseOpenHelper = AccountDatabaseOpenHelper( context, "users_test.db", null, 1)
    }

    public fun createAccount(userEmail: String, userPassword: String, userDisplayName: String) : Boolean
    {
        return testDatabaseOpenHelper.insertData(userEmail, userPassword, userDisplayName)
    }

    public fun getUserId(userEmail: String, userPassword: String) : Int
    {
        return testDatabaseOpenHelper.getUserId(userEmail, userPassword)
    }

    public fun checkUser(userEmail: String) : Boolean
    {
        return testDatabaseOpenHelper.checkUserName(userEmail)
    }
}