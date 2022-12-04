package feature_goals

import account_creation.LocalUserData
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
import shared.Consts

class GoalCreationFragment : Fragment()
{
    private var goalIconImageView: ImageView? = null
    private var goalTypeDropDown: Spinner? = null
    private var goalNameEditText: EditText? = null
    private var goalTargetEditText: EditText? = null
    private var createGoalButton: Button? = null

    private var adapter: ArrayAdapter<GoalTypes>? = null
    private var localUser: LocalUserData? = null

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
            //GoalManager.setUpDatabase(it)

            // Set up Goal Type Drop Down
            adapter = ArrayAdapter<GoalTypes>(it, android.R.layout.simple_spinner_item, GoalTypes.values())
            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        localUser = arguments?.getSerializable(Consts.LOCAL_USER_DATA) as LocalUserData

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

            GoalManager.createGoal(goalType, goalName, goalTarget, "0",localUser!!.userId)

            val updatedGoals = GoalManager.fetchGoals(localUser!!.userId)

            val updatedGoalBundle = Bundle()
            updatedGoalBundle.putSerializable(Consts.LOCAL_USER_DATA, localUser)
            updatedGoalBundle.putSerializable(Consts.USER_GOALS, updatedGoals)

            val goalManagementFragment = GoalManagementFragment()
            goalManagementFragment.arguments = updatedGoalBundle

            it.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, goalManagementFragment, "")
                ?.commit()
        }
    }
}