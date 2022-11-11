package account_creation

import android.content.Intent
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.goal_tracker.R
import goal_creation.GoalManagement
import android.database.sqlite.SQLiteDatabase
import database.TestDatabaseOpenHelper

class LoginActivity : AppCompatActivity()
{
    private var loginButton: Button? = null
    private var emailTextView: EditText? = null
    private var passwordTextView: EditText? = null

    private var userEmail: String? = null
    private var userPassword: String? = null

    private lateinit var testDatabaseOpenHelper : TestDatabaseOpenHelper
    private lateinit var accountDatabase: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        // Open A Read-Only connection to the Accounts Database
        testDatabaseOpenHelper = TestDatabaseOpenHelper(this, "account_details.db", null, 1)
        accountDatabase = testDatabaseOpenHelper.readableDatabase

        loginButton = findViewById(R.id.login)

        emailTextView = findViewById(R.id.editEmailAddress)
        passwordTextView = findViewById(R.id.editPassword)

        loginButton?.setOnClickListener { attemptLogin() }

        /*TODO: Fix issue where user needs to click NEXT and DONE buttons
        *  on keyboard in order for the app to collect the email and password */

        emailTextView?.setOnEditorActionListener ( object: TextView.OnEditorActionListener
        {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if(p1 == EditorInfo.IME_ACTION_NEXT)
                {
                    userEmail = emailTextView?.text.toString()
                    return true
                }
                return false
            }
        })

        passwordTextView?.setOnEditorActionListener ( object: TextView.OnEditorActionListener
        {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if(p1 == EditorInfo.IME_ACTION_DONE)
                {
                    userPassword = passwordTextView?.text.toString()
                    return true
                }
                return false
            }
        })
    }

    private fun attemptLogin()
    {
        Log.d("Logging App", "Attempting Login")
        Log.d("Logging App", "User Email $userEmail")
        Log.d("Logging App", "User Password $userPassword")

        val table_name: String = "account_details"
        val columns: Array<String> = arrayOf("ID", "USER_EMAIL", "USER_PASSWORD", "USER_DISPLAY_NAME")
        val where: String? = "USER_EMAIL = ?"
        val where_args: Array<String>? = arrayOf("$userEmail")
        val group_by: String? = null
        val having: String? = null
        val order_by: String? = null

        var c: Cursor = accountDatabase.query(table_name, columns, where, where_args, group_by, having, order_by)

        var userFound: Boolean = false

        var text: String = ""

        c.moveToFirst()
        for(i in 0 until c.count)
        {
            userFound = c.getInt(1).toString() == userEmail && c.getInt(2).toString() == userPassword
            text += c.getInt(1).toString() + " " + c.getInt(2).toString()
            Log.d("Logging App", text)
            c.moveToNext()
        }

        if(userFound)
        {
            Log.d("Logging App", "Account Found, Login Successful")

            var intent = Intent(this, GoalManagement::class.java)
            startActivity(intent)
        }
        else
        {
            Log.d("Logging App", "Account Not Found, Login Failed")

            var intent = Intent(this, FailedLogin::class.java)
            startActivity(intent)
        }
    }
}