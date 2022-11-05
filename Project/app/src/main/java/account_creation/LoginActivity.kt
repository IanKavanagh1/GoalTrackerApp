package account_creation

import android.content.Intent
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

class LoginActivity : AppCompatActivity()
{
    private var loginButton: Button? = null
    private var emailTextView: EditText? = null
    private var passwordTextView: EditText? = null

    private var userEmail: String? = null
    private var userPassword: String? = null

    /*TODO: Move this to a manager so we can access it in other files
    *  rather than needing to create it each time*/
    private var accountsDirectory: AccountsDirectory = AccountsDirectory()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

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

        var testUser = UserAccount(userEmail, userPassword)

        accountsDirectory.addAccount(testUser)

        if(accountsDirectory.checkAccounts(testUser))
        {
            Log.d("Logging App", "Account Found, Login Successful")

            var intent = Intent(this, GoalManagement::class.java)
            startActivity(intent)
        }
        else
        {
            /*TODO: Make this user facing*/
            Log.d("Logging App", "Account Not Found, Login Failed")
        }
    }
}