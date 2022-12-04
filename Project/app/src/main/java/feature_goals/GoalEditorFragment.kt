package feature_goals

import account_creation.LocalUserData
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentGoalEditorBinding
import shared.Consts

class GoalEditorFragment : Fragment() {

    private var selectedGoalName: TextView? = null
    private var updateGoalButton: Button? = null
    private var deleteGoalButton: Button? = null
    private var localUser: LocalUserData? = null

    private var updatedGoalName = ""

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
            GoalManager.setUpDatabase(it)
        }

        selectedGoalName = view?.findViewById(R.id.selectedGoalName)

        val selectedGoal = arguments?.getSerializable(Consts.SELECTED_GOAL) as GoalDataModel

        localUser = arguments?.getSerializable(Consts.LOCAL_USER_DATA) as LocalUserData

        selectedGoalName?.text = getString(R.string.goal_name_editor, selectedGoal.goalName)

        updateGoalButton = view?.findViewById(R.id.updateGoalButton)

        updateGoalButton?.setOnClickListener { updateGoal(selectedGoal.goalId) }

        deleteGoalButton = view?.findViewById(R.id.deleteGoalButton)

        deleteGoalButton?.setOnClickListener { deleteGoal(selectedGoal.goalId) }
    }

    private fun updateGoal(goalId: Int)
    {
        updatedGoalName = selectedGoalName?.text.toString()
        GoalManager.updateGoal(goalId, updatedGoalName)

        goBackToGoalManagementFragment()
    }

    private fun deleteGoal(goalId: Int)
    {
        GoalManager.removeGoal(goalId)

        goBackToGoalManagementFragment()
    }

    private fun goBackToGoalManagementFragment()
    {
        val goalManagementFragment = GoalManagementFragment()

        val updatedGoalBundle = Bundle()
        updatedGoalBundle.putSerializable(Consts.USER_GOALS, GoalManager.fetchGoals(localUser!!.userId))
        updatedGoalBundle.putSerializable(Consts.LOCAL_USER_DATA, localUser)
        goalManagementFragment.arguments = updatedGoalBundle

        activity?.let {
            it.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frameLayout, goalManagementFragment, "")
                ?.commit()
        }
    }
}