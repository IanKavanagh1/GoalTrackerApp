package feature_goals

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentGoalEditorBinding
import shared.Consts

class GoalEditorFragment : Fragment() {

    private var selectedGoalName: TextView? = null

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

        selectedGoalName?.text = getString(R.string.goal_name_editor, selectedGoal.goalName)
    }
}