package account_creation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.goal_tracker.MainActivity
import com.example.goal_tracker.R

class LoginActivity : AppCompatActivity()
{
    private var loginButton: Button? = null
    private var emailTextView: EditText? = null
    private var passwordTextView: EditText? = null
    private var goToRegisterActBtn: Button? = null

    private var userEmail: String = ""
    private var userPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        AccountManager.setUpDatabase(this)

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
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this, "Failed To Login! Please Check Login Details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToRegisterActivity()
    {
        var intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}