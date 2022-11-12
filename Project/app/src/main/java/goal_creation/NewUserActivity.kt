package goal_creation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.goal_tracker.R

class NewUserActivity : AppCompatActivity()
{
    private var createGoalButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        createGoalButton = findViewById(R.id.create_goal)

        createGoalButton?.setOnClickListener { createGoal() }
    }

    private fun createGoal()
    {
        var intent = Intent(this, GoalCreationActivity::class.java)
        startActivity(intent)
    }
}