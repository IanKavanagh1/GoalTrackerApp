package account_creation

import android.content.Context
import database.AccountDatabaseOpenHelper

object AccountManager
{
    private lateinit var accountDatabaseOpenHelper: AccountDatabaseOpenHelper

    fun setUpDatabase(context: Context)
    {
        //TODO: Use database name from const file
        accountDatabaseOpenHelper = AccountDatabaseOpenHelper( context, "users_test.db", null, 1)
    }

    fun createAccount(userEmail: String, userPassword: String, userDisplayName: String) : Boolean
    {
        return accountDatabaseOpenHelper.insertData(userEmail, userPassword, userDisplayName)
    }

    fun getUserId(userEmail: String, userPassword: String) : Int
    {
        return accountDatabaseOpenHelper.getUserId(userEmail, userPassword)
    }

    fun getUserDisplayName(userId: Int) : String
    {
        return accountDatabaseOpenHelper.getUserDisplayName(userId)
    }

    fun checkUser(userEmail: String) : Boolean
    {
        return accountDatabaseOpenHelper.checkUserName(userEmail)
    }

    fun checkUserEmailAndPassword(userEmail: String, userPassword: String) : Boolean
    {
        return accountDatabaseOpenHelper.checkUserNameAndPassword(userEmail, userPassword)
    }

    fun getUserEmailAndDisplayName(userId: Int) : Array<String>
    {
        return accountDatabaseOpenHelper.getUserEmailAndDisplayName(userId)
    }
}