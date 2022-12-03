package feature_goals

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
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

        selectedGoalName = view?.findViewById(R.id.selectedGoalName)

        val selectedGoal = arguments?.getSerializable(Consts.SELECTED_GOAL) as GoalDataModel

        Log.d("Goal Editor","${selectedGoal.goalId}")

        selectedGoalName?.text = getString(R.string.goal_name_editor, selectedGoal.goalName)

        updateGoalButton = view?.findViewById(R.id.updateGoalButton)

        updateGoalButton?.setOnClickListener { updateGoal() }
    }

    private fun updateGoal()
    {
        Log.d("Goal Editor","Update Goal and Go Back To Goal List")
    }
}