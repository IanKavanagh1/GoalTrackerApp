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
    // Variables to store all the UI elements
    private var registerButton: Button? = null
    private var emailTextView: EditText? = null
    private var passwordTextView: EditText? = null
    private var displayNameTextView: EditText? = null
    private var goToLoginActBtn: Button? = null

    // Variables to store the user data
    private var userEmail: String = ""
    private var userPassword: String = ""
    private var userDisplayName: String = ""
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialise the account database
        AccountManager.setUpDatabase(this)

        // Gather all the UI elements
        registerButton = findViewById(R.id.register)
        goToLoginActBtn = findViewById(R.id.goToLoginBtn)

        emailTextView = findViewById(R.id.editEmailAddress)
        passwordTextView = findViewById(R.id.editPassword)
        displayNameTextView = findViewById(R.id.editDisplayName)

        // set up listener for register button click
        registerButton?.setOnClickListener { createAccount() }

        // set up listener for login button click
        goToLoginActBtn?.setOnClickListener { goToLoginActivity() }
    }

    private fun createAccount()
    {
        // Grab the email and password entered by the user
        userEmail = emailTextView?.text.toString()
        userPassword = passwordTextView?.text.toString()
        userDisplayName = displayNameTextView?.text.toString()

        // Make sure they are not empty
        if(userEmail == "" || userPassword == "" || userDisplayName == "")
        {
            // Display a notification to the user that they are missing fields
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show()
        }
        else
        {
            // Once verified the fields are not empty we can check the email to make sure it doesn't
            // exist already
            if(AccountManager.checkUser(userEmail))
            {
                // if it does, display a notification letting the user know the email is already is use
                Toast.makeText(this, "Email Already Exists, Please Sign In", Toast.LENGTH_SHORT).show()
            }
            else
            {
                // otherwise we can create the account
                if (AccountManager.createAccount(userEmail, userPassword, userDisplayName))
                {
                    // If the createAccount returns true we can display a successful notification
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()

                    // get the shared prefs and store the new user data
                    val sharedPreferences = getSharedPreferences(Consts.USER_PREFS, MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    editor.apply {
                        // flag user as logged ing
                        putBoolean(Consts.PREFS_LOGGED_IN, true)

                        // remove and updated stored user id
                        remove(Consts.PREFS_USER_ID)
                        id = AccountManager.getUserId(userEmail,userPassword)
                        putInt(Consts.PREFS_USER_ID, id)

                        // remove and updated stored user display name
                        remove(Consts.PREFS_USER_DISPLAY_NAME)
                        putString(Consts.PREFS_USER_DISPLAY_NAME, AccountManager.getUserDisplayName(id))
                    }.apply()

                    // go to main act and shared the local user data so the main act can use it
                    goToMainActivity(LocalUserData(id, userEmail, userDisplayName, userPassword))
                }
                else
                {
                    // If the create account returns false, we can display a notification to let
                    // the user know the account was not created
                    Toast.makeText(this, "Failed To Create Account!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToLoginActivity()
    {
        // Transition to the login act
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        // close the register act
        finish()
    }

    private fun goToMainActivity(localUserData: LocalUserData?)
    {
        val intent = Intent(this, MainActivity::class.java)

        // verify the data is not null
        if(localUserData != null)
        {
            // share the data with the main act
            intent.putExtra(Consts.LOCAL_USER_DATA, localUserData)
        }

        startActivity(intent)
        // close the register act
        finish()
    }
}