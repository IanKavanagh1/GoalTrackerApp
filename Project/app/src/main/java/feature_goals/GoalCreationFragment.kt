package feature_goals

import account_creation.LocalUserData
import android.graphics.drawable.Icon
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentGoalCreationBinding
import shared.Consts

class GoalCreationFragment : Fragment()
{
    private lateinit var goalIconImageView: ImageView
    private lateinit var goalTypeDropDown: Spinner
    private lateinit var goalNameEditText: EditText
    private lateinit var goalTargetEditText: EditText
    private lateinit var createGoalButton: Button

    private lateinit var adapter: ArrayAdapter<GoalTypes>
    private lateinit var localUser: LocalUserData
    private lateinit var selectedGoalType: GoalTypes

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

            // Set up Goal Type Drop Down
            adapter = ArrayAdapter<GoalTypes>(it, android.R.layout.simple_spinner_item, GoalTypes.values())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // get local user from arguments passed in
        localUser = arguments?.getSerializable(Consts.LOCAL_USER_DATA) as LocalUserData

        // Get all UI Objects
        goalIconImageView = view!!.findViewById(R.id.gIcon)
        goalTypeDropDown = view!!.findViewById(R.id.gType)
        goalNameEditText = view!!.findViewById(R.id.gName)
        goalTargetEditText = view!!.findViewById(R.id.gTarget)
        createGoalButton = view!!.findViewById(R.id.createGoalBtn)

        // Set up spinner options
        goalTypeDropDown.adapter = adapter

        goalTypeDropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, pos: Int, id: Long)
            {
                selectedGoalType = adapter!!.getItemAtPosition(pos) as GoalTypes

                when(selectedGoalType)
                {
                    GoalTypes.Fitness -> {
                        activity?.let {
                            val icon = Icon.createWithResource(it, R.drawable.ic_baseline_fitness_center_24)
                            goalIconImageView.setImageIcon(icon)
                        }
                    }
                    GoalTypes.HealthEating -> {
                    activity?.let {
                        val icon = Icon.createWithResource(it, R.drawable.ic_baseline_food_bank_24)
                        goalIconImageView.setImageIcon(icon)
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        // set up button listener
        createGoalButton.setOnClickListener { createGoal() }
    }

    private fun createGoal()
    {
        val goalType = selectedGoalType

        val goalName = goalNameEditText.text.toString()
        val goalTarget = goalTargetEditText.text.toString()

        activity?.let {

            // create goal
            GoalManager.createGoal(goalType.ordinal, goalName, goalTarget, "0",localUser.userId)

            // get updated goals
            val updatedGoals = GoalManager.fetchGoals(localUser.userId)

            // create updated goal bundle
            val updatedGoalBundle = Bundle()

            // provide the local user and update goal to the bundle
            updatedGoalBundle.putSerializable(Consts.LOCAL_USER_DATA, localUser)
            updatedGoalBundle.putSerializable(Consts.USER_GOALS, updatedGoals)

            // share the updated goal bundle to the goal management fragment
            val goalManagementFragment = GoalManagementFragment()
            goalManagementFragment.arguments = updatedGoalBundle

            // go to management fragment
            it.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, goalManagementFragment, "")
                ?.commit()
        }
    }
}