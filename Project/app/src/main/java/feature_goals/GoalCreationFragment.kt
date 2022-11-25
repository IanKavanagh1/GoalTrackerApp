package feature_goals

import account_creation.AccountManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentGoalCreationBinding

class GoalCreationFragment : Fragment()
{
    private var goalManager: GoalManager? = null
    private var goalIconImageView: ImageView? = null
    private var goalTypeDropDown: Spinner? = null
    private var goalNameEditText: EditText? = null
    private var goalTargetEditText: EditText? = null
    private var createGoalButton: Button? = null

    private var adapter: ArrayAdapter<GoalTypes>? = null

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

            // Set up Goal Type Drop Down
            adapter = ArrayAdapter<GoalTypes>(it, android.R.layout.simple_spinner_item, GoalTypes.values())
            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Get all UI Objects
        goalIconImageView = view?.findViewById(R.id.gIcon)
        goalTypeDropDown = view?.findViewById(R.id.gType)
        goalNameEditText = view?.findViewById(R.id.gName)
        goalTargetEditText = view?.findViewById(R.id.gTarget)
        createGoalButton = view?.findViewById(R.id.createGoalBtn)

        createGoalButton?.setOnClickListener { createGoal() }
    }

    private fun createGoal()
    {
        //TODO: HAVE GOAL TYPE A USER ENTERED PIECE OF DATA
        var goalType = 0

        var goalName = goalNameEditText?.text.toString()
        var goalTarget = goalTargetEditText?.text.toString()

        //TODO: GET ACTUAL USER ID From the AccountManager
        var userId = AccountManager.getUserId("", "")

        goalManager?.createGoal(goalType, goalName, goalTarget, userId)

        //TODO: TEST TO MAKE SURE THE GOAL WAS ADDED SUCCESSFULLY

        // get all the goals associated with the user
        goalManager?.fetchGoals(userId)

        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, GoalManagementFragment(), "")
            ?.addToBackStack("null")?.commit()
    }
}