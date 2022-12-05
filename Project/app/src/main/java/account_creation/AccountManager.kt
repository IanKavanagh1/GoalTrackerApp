package account_creation

import android.content.Context
import database.AccountDatabaseOpenHelper
import shared.Consts

object AccountManager
{
    // Variables to store the Account Database and the DatabaseOpenHelper
    private const val dbName = Consts.USER_DATABASE + ".db"
    private lateinit var accountDatabaseOpenHelper: AccountDatabaseOpenHelper

    fun setUpDatabase(context: Context)
    {
        // Initialise the account database
        accountDatabaseOpenHelper = AccountDatabaseOpenHelper( context, dbName, null, 1)
    }

    // Returns true if the new user data was successfully added to the database
    fun createAccount(userEmail: String, userPassword: String, userDisplayName: String) : Boolean
    {
        // Add new user data to the database
        return accountDatabaseOpenHelper.insertData(userEmail, userPassword, userDisplayName)
    }

    // Returns with the userId if found, or -1 if no user was found
    fun getUserId(userEmail: String, userPassword: String) : Int
    {
        // Try to search for the userID using the provided email and password
        return accountDatabaseOpenHelper.getUserId(userEmail, userPassword)
    }

    // Returns the user display name if found, an empty string if not
    fun getUserDisplayName(userId: Int) : String
    {
        // Try to get the userDisplay name using the provided userId
        return accountDatabaseOpenHelper.getUserDisplayName(userId)
    }

    // Returns true if the user email exists in the database, false if not
    fun checkUser(userEmail: String) : Boolean
    {
        // Search the database to try find the userEmail provided
        // This is to prevent multiple users having the same email
        return accountDatabaseOpenHelper.checkUserName(userEmail)
    }

    // Returns true if the email and password match that found in the database for the user
    // Used to authenticate the user login
    fun checkUserEmailAndPassword(userEmail: String, userPassword: String) : Boolean
    {
        // Check the provided email and password to make sure they match what is in the database
        return accountDatabaseOpenHelper.checkUserNameAndPassword(userEmail, userPassword)
    }

    // Returns an Array of strings with the email and display name for the user,
    // an empty array if the user is not found
    fun getUserEmailAndDisplayName(userId: Int) : Array<String>
    {
        // Try to get the email and display name for the provided userId
        return accountDatabaseOpenHelper.getUserEmailAndDisplayName(userId)
    }
}