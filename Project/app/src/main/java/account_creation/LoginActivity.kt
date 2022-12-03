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
    private var loginButton: Button? = null
    private var emailTextView: EditText? = null
    private var passwordTextView: EditText? = null
    private var goToRegisterActBtn: Button? = null

    private var userEmail: String = ""
    private var userPassword: String = ""
    private var userDisplayName: String = ""
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        AccountManager.setUpDatabase(this)

        if(checkIfUserIsLoggedIn())
        {
            val sharedPreferences = getSharedPreferences(Consts.USER_PREFS, MODE_PRIVATE)
            val id = sharedPreferences.getInt(Consts.PREFS_USER_ID, -1)

            val userEmailAndDisplay = AccountManager.getUserEmailAndDisplayName(id)

            goToMainActivity(LocalUserData(id, userEmailAndDisplay[0], userEmailAndDisplay[1]))
        }

        loginButton = findViewById(R.id.login)
        goToRegisterActBtn = findViewById(R.id.signUpBtn)

        emailTextView = findViewById(R.id.editEmailAddress)
        passwordTextView = findViewById(R.id.editPassword)

        loginButton?.setOnClickListener { attemptLogin() }

        goToRegisterActBtn?.setOnClickListener { goToRegisterActivity() }
    }

    private fun attemptLogin()
    {
        userEmail = emailTextView?.text.toString()
        userPassword = passwordTextView?.text.toString()

        if(userEmail == "" || userPassword == "" )
        {
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show()
        }
        else
        {
            if (AccountManager.checkUserEmailAndPassword(userEmail, userPassword))
            {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                val sharedPreferences = getSharedPreferences(Consts.USER_PREFS, MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                editor.apply {
                    putBoolean(Consts.PREFS_LOGGED_IN, true)
                    remove(Consts.PREFS_USER_ID)
                    id = AccountManager.getUserId(userEmail,userPassword)
                    putInt(Consts.PREFS_USER_ID, id)
                    remove(Consts.PREFS_USER_DISPLAY_NAME)
                    userDisplayName = AccountManager.getUserDisplayName(id)
                    putString(Consts.PREFS_USER_DISPLAY_NAME, userDisplayName)
                }.apply()

                goToMainActivity(LocalUserData(id, userEmail, userDisplayName))
            }
            else
            {
                Toast.makeText(this, "Failed To Login! Please Check Login Details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToRegisterActivity()
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finishAndRemoveTask()
    }

    private fun checkIfUserIsLoggedIn() : Boolean
    {
        val sharedPreferences = getSharedPreferences(Consts.USER_PREFS, MODE_PRIVATE)

        return sharedPreferences.getBoolean(Consts.PREFS_LOGGED_IN, false)
    }

    private fun goToMainActivity(localUserData: LocalUserData?)
    {
        val intent = Intent(this, MainActivity::class.java)

        if(localUserData != null)
        {
            intent.putExtra(Consts.LOCAL_USER_DATA, localUserData)
        }

        startActivity(intent)
        finish()
    }
}