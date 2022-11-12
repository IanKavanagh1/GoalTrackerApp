package goal_creation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.goal_tracker.R

class GoalCreationActivity : AppCompatActivity()
{
    private var goalManager: GoalManager = GoalManager(this)
    private var goalNameEditText: EditText? = null
    private var goalTargetEditText: EditText? = null
    private var goalCurrentEditText: EditText? = null
    private var createGoalButton: Button? = null

    private var goalName: String = ""
    private var goalTarget: String = ""
    private var goalCurrent: String = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_creation)

        goalNameEditText = findViewById(R.id.editGoalName)
        goalTargetEditText = findViewById(R.id.editGoalTarget)
        goalCurrentEditText = findViewById(R.id.editGoalCurrent)

        createGoalButton = findViewById(R.id.create)

        goalNameEditText?.setOnEditorActionListener ( object: TextView.OnEditorActionListener
        {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if(p1 == EditorInfo.IME_ACTION_NEXT)
                {
                    goalName = goalNameEditText?.text.toString()
                    return true
                }
                return false
            }
        })

        goalTargetEditText?.setOnEditorActionListener ( object: TextView.OnEditorActionListener
        {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if(p1 == EditorInfo.IME_ACTION_NEXT)
                {
                    goalTarget = goalTargetEditText?.text.toString()
                    return true
                }
                return false
            }
        })

        goalCurrentEditText?.setOnEditorActionListener ( object: TextView.OnEditorActionListener
        {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if(p1 == EditorInfo.IME_ACTION_DONE)
                {
                    goalCurrent = goalCurrentEditText?.text.toString()
                    return true
                }
                return false
            }
        })

        createGoalButton?.setOnClickListener { createGoal(goalName,goalTarget,goalCurrent) }
    }

    private fun createGoal(goalName: String, goalTarget: String, goalCurrent: String)
    {
        goalManager.createGoal(goalName, goalTarget, goalCurrent)

        //TEST TO MAKE SURE THE GOAL WAS ADDED SUCCESSFULLY
        goalManager.fetchGoals(goalName)

        //TODO: Add Checks to make sure goal was created successfully before transition
        var intent = Intent(this, GoalManagementActivity::class.java)
        startActivity(intent)
    }
}