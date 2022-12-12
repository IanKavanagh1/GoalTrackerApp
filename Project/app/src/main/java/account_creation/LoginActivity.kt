package account_creation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.goal_tracker.MainActivity
import com.example.goal_tracker.R
import shared.Consts

class LoginActivity : AppCompatActivity()
{
    // Variables to store all the UI elements
    private lateinit var loginButton: Button
    private lateinit var emailTextView: EditText
    private lateinit var passwordTextView: EditText
    private lateinit var goToRegisterActBtn: Button

    // Variables to store the user data
    private var userEmail: String = ""
    private var userPassword: String = ""
    private var userDisplayName: String = ""
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        // Initialise the account database
        AccountManager.setUpDatabase(this)

        // Check the shared preferences to see if the user is already logged in
        if(checkIfUserIsLoggedIn())
        {
            // If they are we can get the userId for the logged in user via the shared prefs
            val sharedPreferences = getSharedPreferences(Consts.USER_PREFS, MODE_PRIVATE)
            val id = sharedPreferences.getInt(Consts.PREFS_USER_ID, -1)

            // Get the email and display name from the AccountManager using the userId from shared
            // prefs
            val userEmailDisplayAndPassword = AccountManager.getUserEmailDisplayNameAndPassword(id)

            // Transition to the main app (auto-login)
            goToMainActivity(LocalUserData(id, userEmailDisplayAndPassword[0], userEmailDisplayAndPassword[2], userEmailDisplayAndPassword[1]))
        }

        // Gather all the UI elements
        loginButton = findViewById(R.id.login)
        goToRegisterActBtn = findViewById(R.id.signUpBtn)

        emailTextView = findViewById(R.id.editEmailAddress)
        passwordTextView = findViewById(R.id.editPassword)

        // set up listener for login button click
        loginButton.setOnClickListener { attemptLogin() }

        // set up listener for register button click
        goToRegisterActBtn.setOnClickListener { goToRegisterActivity() }
    }

    private fun attemptLogin()
    {
        // Grab the email and password entered by the user
        userEmail = emailTextView.text.toString()
        userPassword = passwordTextView.text.toString()

        // Make sure they are not empty
        if(userEmail == "" || userPassword == "" )
        {
            // Display a notification to the user that they are missing fields
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show()
        }
        else
        {
            // Once verified the fields are not empty we can check the email and password
            if (AccountManager.checkUserEmailAndPassword(userEmail, userPassword))
            {
                // If it matches what is stored in the database we can log in
                // Display successful login notification
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                // Store the logged in user data to the shared prefs
                // This is so that we can handle the auto login for any account that is logged into the app
                val sharedPreferences = getSharedPreferences(Consts.USER_PREFS, MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                // Apply the user data to the user prefs
                editor.apply {
                    // flag user as logged in
                    putBoolean(Consts.PREFS_LOGGED_IN, true)

                    // remove and update the logged in user id
                    remove(Consts.PREFS_USER_ID)
                    id = AccountManager.getUserId(userEmail,userPassword)
                    putInt(Consts.PREFS_USER_ID, id)

                    // remove and update the logged in user display name
                    remove(Consts.PREFS_USER_DISPLAY_NAME)
                    userDisplayName = AccountManager.getUserDisplayName(id)
                    putString(Consts.PREFS_USER_DISPLAY_NAME, userDisplayName)
                }.apply()

                // Transition to the main activity and pass in the Local User data
                // so that the main activity can access it without needing to call to a database
                goToMainActivity(LocalUserData(id, userEmail, userDisplayName, userPassword))
            }
            else
            {
                // Otherwise show a notification to let the user know they failed to log in
                Toast.makeText(this, "Failed To Login! Please Check Login Details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToRegisterActivity()
    {
        // Transition to the register activity
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        // close the login activity
        finish()
    }

    // Returns true if the logged in flag from user prefs is true, false otherwise
    private fun checkIfUserIsLoggedIn() : Boolean
    {
        // check if the user is already logged in so that they will automatically go to the app
        // home screen rather than needing to login every time they start the app
        val sharedPreferences = getSharedPreferences(Consts.USER_PREFS, MODE_PRIVATE)

        return sharedPreferences.getBoolean(Consts.PREFS_LOGGED_IN, false)
    }

    private fun goToMainActivity(localUserData: LocalUserData?)
    {
        // transition to the main activity
        val intent = Intent(this, MainActivity::class.java)

        // verify that the data is not null
        if(localUserData != null)
        {
            // add the user data to the intent so that the main act can use it
            intent.putExtra(Consts.LOCAL_USER_DATA, localUserData)
        }

        startActivity(intent)
        // close login act
        finish()
    }
}