package account_creation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.goal_tracker.MainActivity
import com.example.goal_tracker.R

class FailedLogin : AppCompatActivity()
{
    private var retryButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_failed_login)

        retryButton = findViewById(R.id.retry_button)

        retryButton?.setOnClickListener { retryLogin() }
    }

    private fun retryLogin()
    {
        // Bring the user back to the main screen
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}