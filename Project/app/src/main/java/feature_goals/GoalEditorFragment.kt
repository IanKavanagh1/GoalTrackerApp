package feature_goals

import account_creation.LocalUserData
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentGoalEditorBinding
import shared.Consts

class GoalEditorFragment : Fragment() {

    private lateinit var selectedGoalName: TextView
    private lateinit var selectedGoalProgress: TextView
    private lateinit var selectedGoalType: Spinner
    private lateinit var updateGoalButton: Button
    private lateinit var deleteGoalButton: Button
    private var localUser: LocalUserData? = null

    private var updatedGoalName = ""
    private var updatedGoalProgress = ""
    private var updatedGoalType: GoalTypes? = null

    private lateinit var adapter: ArrayAdapter<GoalTypes>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        val binding = FragmentGoalEditorBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activity?.let {

            // Set up Goal Type Drop Down
            adapter = ArrayAdapter<GoalTypes>(it, android.R.layout.simple_spinner_item, GoalTypes.values())
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        selectedGoalName = view!!.findViewById(R.id.selectedGoalName)
        selectedGoalProgress = view!!.findViewById(R.id.selectedGoalProgress)
        selectedGoalType = view!!.findViewById(R.id.selectedGoalType)

        selectedGoalType.adapter = adapter

        selectedGoalType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, pos: Int, id: Long)
            {
                updatedGoalType = adapter!!.getItemAtPosition(pos) as GoalTypes?
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        // grab the selected goal from shared arguments
        val selectedGoal = arguments?.getSerializable(Consts.SELECTED_GOAL) as GoalDataModel

        // grab the local user data from the shared arguments
        localUser = arguments?.getSerializable(Consts.LOCAL_USER_DATA) as LocalUserData

        selectedGoalName.text = getString(R.string.goal_name_editor, selectedGoal.goalName)
        selectedGoalProgress.text = getString(R.string.goal_progress_value, selectedGoal.goalProgress)

        updateGoalButton = view!!.findViewById(R.id.updateGoalButton)

        updateGoalButton.setOnClickListener { updateGoal(selectedGoal.goalId) }

        deleteGoalButton = view!!.findViewById(R.id.deleteGoalButton)

        deleteGoalButton.setOnClickListener { deleteGoal(selectedGoal.goalId) }
    }

    private fun updateGoal(goalId: Int)
    {
        // get the updated goal name from the user
        updatedGoalName = selectedGoalName.text.toString()
        updatedGoalProgress = selectedGoalProgress.text.toString()

        // update goal
        GoalManager.updateGoal(goalId, updatedGoalName, updatedGoalProgress, updatedGoalType!!)

        // transition to the management fragment
        goBackToGoalManagementFragment()
    }

    private fun deleteGoal(goalId: Int)
    {
        // delete goal with id provided
        GoalManager.removeGoal(goalId)

        goBackToGoalManagementFragment()
    }

    private fun goBackToGoalManagementFragment()
    {
        // create goal management fragment
        val goalManagementFragment = GoalManagementFragment()

        // create new bundle for goal ui
        val updatedGoalBundle = Bundle()

        // store updated data
        updatedGoalBundle.putSerializable(Consts.USER_GOALS, GoalManager.fetchGoals(localUser!!.userId))
        updatedGoalBundle.putSerializable(Consts.LOCAL_USER_DATA, localUser)
        goalManagementFragment.arguments = updatedGoalBundle

        // transition back to the goal management ui
        activity?.let {
            it.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frameLayout, goalManagementFragment, "")
                ?.commit()
        }
    }
}