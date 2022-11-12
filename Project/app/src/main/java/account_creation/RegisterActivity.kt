package account_creation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.goal_tracker.R
import goal_creation.GoalManagement
import android.util.Log

class RegisterActivity : AppCompatActivity()
{
    private var registerButton: Button? = null
    private var emailTextView: EditText? = null
    private var passwordTextView: EditText? = null

    private var userEmail: String = ""
    private var userPassword: String = ""

    //TODO: Make manager static so we don't need an instance each time we want to use it
    private var accountManager: AccountManager = AccountManager(this)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.register)

        emailTextView = findViewById(R.id.editEmailAddress)
        passwordTextView = findViewById(R.id.editPassword)

        registerButton?.setOnClickListener { createAccount() }

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

    private fun createAccount()
    {
        accountManager.createAccount(userEmail, userPassword, "")

        if(accountManager.fetchAccount(userEmail, userPassword))
        {
            Log.d("Register Activity", "Account Created, Auto Login Success")
            var intent = Intent(this, GoalManagement::class.java)
            startActivity(intent)
        }
        else
        {
            Log.d("Register Activity", "Account Not Found, Auto Login Failed")

            var intent = Intent(this, FailedLogin::class.java)
            startActivity(intent)
        }
    }
}