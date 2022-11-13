package goal_creation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.goal_tracker.MainActivity
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentGoalCreationBinding

class GoalCreationFragment : Fragment()
{
    private var goalManager: GoalManager? = null
    private var goalNameEditText: EditText? = null
    private var goalTargetEditText: EditText? = null
    private var goalCurrentEditText: EditText? = null
    private var createGoalButton: Button? = null

    private var goalName: String = ""
    private var goalTarget: String = ""
    private var goalCurrent: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGoalCreationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activity?.let {
            goalManager = GoalManager(it)
        }

        goalNameEditText = view?.findViewById(R.id.editGoalName)
        goalTargetEditText = view?.findViewById(R.id.editGoalTarget)
        goalCurrentEditText = view?.findViewById(R.id.editGoalCurrent)

        createGoalButton = view?.findViewById(R.id.create)

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
        goalManager?.createGoal(goalName, goalTarget, goalCurrent)

        //TODO: TEST TO MAKE SURE THE GOAL WAS ADDED SUCCESSFULLY
        goalManager?.fetchGoals(goalName)

        MainActivity().replaceFragment(GoalManagementFragment())
    }
}