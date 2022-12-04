package account_creation

import android.content.Context
import database.AccountDatabaseOpenHelper
import shared.Consts

object AccountManager
{
    private const val dbName = Consts.USER_DATABASE + ".db"
    private lateinit var accountDatabaseOpenHelper: AccountDatabaseOpenHelper

    fun setUpDatabase(context: Context)
    {
        accountDatabaseOpenHelper = AccountDatabaseOpenHelper( context, dbName, null, 1)
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