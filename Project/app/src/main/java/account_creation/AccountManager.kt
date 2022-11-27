package account_creation

import android.content.Context
import database.AccountDatabaseOpenHelper

object AccountManager
{
    private lateinit var accountDatabaseOpenHelper: AccountDatabaseOpenHelper

    public fun setUpDatabase(context: Context)
    {
        //TODO: Use database name from const file
        accountDatabaseOpenHelper = AccountDatabaseOpenHelper( context, "users_test.db", null, 1)
    }

    public fun createAccount(userEmail: String, userPassword: String, userDisplayName: String) : Boolean
    {
        return accountDatabaseOpenHelper.insertData(userEmail, userPassword, userDisplayName)
    }

    public fun getUserId(userEmail: String, userPassword: String) : Int
    {
        return accountDatabaseOpenHelper.getUserId(userEmail, userPassword)
    }

    public fun getUserDisplayName(userId: Int) : String
    {
        return accountDatabaseOpenHelper.getUserDisplayName(userId)
    }

    public fun checkUser(userEmail: String) : Boolean
    {
        return accountDatabaseOpenHelper.checkUserName(userEmail)
    }

    public fun checkUserEmailAndPassword(userEmail: String, userPassword: String) : Boolean
    {
        return accountDatabaseOpenHelper.checkUserNameAndPassword(userEmail, userPassword)
    }
}