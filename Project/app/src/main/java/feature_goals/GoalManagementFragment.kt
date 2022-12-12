package feature_goals

import account_creation.LocalUserData
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentGoalManagmentBinding
import shared.Consts

class GoalManagementFragment : Fragment(), GoalRecyclerViewInterface
{
    private lateinit var goalListView: RecyclerView
    private lateinit var adapter: GoalRecyclerViewAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var addGoalBtn: Button
    private lateinit var welcomeBackTextView: TextView
    private lateinit var emptyGoalsTextView: TextView

    private var userGoals : ArrayList<GoalDataModel>? = null
    private var localUser: LocalUserData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentGoalManagmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart()
    {
        super.onStart()

        welcomeBackTextView = view!!.findViewById(R.id.welcomeBackTV)
        addGoalBtn = view!!.findViewById(R.id.addGoalBtn)
        goalListView = view!!.findViewById(R.id.goal_recycler_view)
        emptyGoalsTextView = view!!.findViewById(R.id.emptyGoals)

        activity?.let {

            // grab user data from passed in arguments
            localUser = arguments?.getSerializable(Consts.LOCAL_USER_DATA) as LocalUserData
            welcomeBackTextView.text = getString(R.string.welcome_back_label, localUser?.userDisplayName)

            // grab user goals from passed in arguments
            userGoals = arguments?.getSerializable(Consts.USER_GOALS) as ArrayList<GoalDataModel>

            // Display an empty goals message if the user has no goals
            if(userGoals!!.size < 1)
            {
                emptyGoalsTextView.visibility = View.VISIBLE
            }
            else
            {
                emptyGoalsTextView.visibility = View.INVISIBLE
            }

            // set up list of goals
            adapter = GoalRecyclerViewAdapter(it, userGoals!!, this)

            layoutManager = LinearLayoutManager(it)
        }

        // populate goal list
        goalListView.adapter = adapter
        goalListView.layoutManager = layoutManager

        addGoalBtn.setOnClickListener { goToCreationUI() }
    }

    private fun goToCreationUI()
    {
        // set up creation bundle
        val goalCreationBundle = Bundle()

        // add the user data to the bundle
        goalCreationBundle.putSerializable(Consts.LOCAL_USER_DATA, localUser)

        val goalCreationFragment = GoalCreationFragment()
        // pass the bundle to the creation fragment
        goalCreationFragment.arguments = goalCreationBundle

        // transition to the creation fragment
        activity?.let {
            it.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, goalCreationFragment, "")
                ?.addToBackStack("null")?.commit()
        }
    }

    // Item Click event handler
    override fun onItemClick(position: Int) {

        // get the selected goal
        val selectedGoal = userGoals?.get(position)

        val selectedGoalBundle = Bundle()

        // add the selected goal and user data to a bundle
        selectedGoalBundle.putSerializable(Consts.SELECTED_GOAL, selectedGoal)
        selectedGoalBundle.putSerializable(Consts.LOCAL_USER_DATA, localUser)

        // create editor fragment and pass in bundle
        val goalEditorFragment = GoalEditorFragment()
        goalEditorFragment.arguments = selectedGoalBundle

        // transition to goal editor ui
        activity?.let {
            it.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, goalEditorFragment, "")
                ?.addToBackStack("null")?.commit()
        }
    }
}