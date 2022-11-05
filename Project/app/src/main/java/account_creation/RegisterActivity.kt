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

class RegisterActivity : AppCompatActivity()
{
    private var registerButton: Button? = null
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
        var account = UserAccount(userEmail, userPassword)

        accountsDirectory.addAccount(account)

        /*TODO: Auto Login After Account Is Created*/

        var intent = Intent(this, GoalManagement::class.java)
        startActivity(intent)
    }
}