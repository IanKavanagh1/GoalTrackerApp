package account_creation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.goal_tracker.R
import android.widget.Toast
import com.example.goal_tracker.MainActivity
import shared.Consts

class RegisterActivity : AppCompatActivity()
{
    private var registerButton: Button? = null
    private var emailTextView: EditText? = null
    private var passwordTextView: EditText? = null
    private var displayNameTextView: EditText? = null
    private var goToLoginActBtn: Button? = null

    private var userEmail: String = ""
    private var userPassword: String = ""
    private var userDisplayName: String = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        AccountManager.setUpDatabase(this)

        registerButton = findViewById(R.id.register)
        goToLoginActBtn = findViewById(R.id.goToLoginBtn)

        emailTextView = findViewById(R.id.editEmailAddress)
        passwordTextView = findViewById(R.id.editPassword)
        displayNameTextView = findViewById(R.id.editDisplayName)

        registerButton?.setOnClickListener { createAccount() }

        goToLoginActBtn?.setOnClickListener { goToLoginActivity() }
    }

    private fun createAccount()
    {
        userEmail = emailTextView?.text.toString()
        userPassword = passwordTextView?.text.toString()
        userDisplayName = displayNameTextView?.text.toString()

        if(userEmail == "" || userPassword == "" || userDisplayName == "")
        {
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show()
        }
        else
        {
            if(AccountManager.checkUser(userEmail))
            {
                Toast.makeText(this, "Email Already Exists, Please Sign In", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if (AccountManager.createAccount(userEmail, userPassword, userDisplayName))
                {
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()

                    val sharedPreferences = getSharedPreferences(Consts.USER_PREFS, MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    //TODO: Pass the User Account details to the MainActivity via Explicit Intent
                    editor.apply {
                        putBoolean(Consts.PREFS_LOGGED_IN, true)
                        remove(Consts.PREFS_USER_ID)
                        val id = AccountManager.getUserId(userEmail,userPassword)
                        putInt(Consts.PREFS_USER_ID, id)
                        remove(Consts.PREFS_USER_DISPLAY_NAME)
                        putString(Consts.PREFS_USER_DISPLAY_NAME, AccountManager.getUserDisplayName(id))
                    }.apply()

                    goToMainActivity()
                }
                else
                {
                    Toast.makeText(this, "Failed To Create Account!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToLoginActivity()
    {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToMainActivity()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}