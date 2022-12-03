package feature_goals

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
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
import shared.Consts

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
    ): View {

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
        val goalType = 0

        val goalName = goalNameEditText?.text.toString()
        val goalTarget = goalTargetEditText?.text.toString()

        activity?.let {
            val sharedPreferences = it.getSharedPreferences(Consts.USER_PREFS, AppCompatActivity.MODE_PRIVATE)
            val userId = sharedPreferences.getInt(Consts.PREFS_USER_ID, -1)

            goalManager?.createGoal(goalType, goalName, goalTarget, userId)

            val updatedGoals = goalManager?.fetchGoals(userId)

            val updatedGoalBundle = Bundle()
            updatedGoalBundle.putSerializable(Consts.USER_GOALS, updatedGoals)

            val goalManagementFragment = GoalManagementFragment()
            goalManagementFragment.arguments = updatedGoalBundle

            it.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, goalManagementFragment, "")
                ?.commit()
        }
    }
}