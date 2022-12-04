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

class GoalManagementFragment : Fragment(), GoalRecylerViewInterface
{
    private var goalListView: RecyclerView? = null
    private var adapter: GoalRecyclerViewAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var addGoalBtn: Button? = null
    private var welcomeBackTextView: TextView? = null

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

        welcomeBackTextView = view?.findViewById(R.id.welcomeBackTV)
        addGoalBtn = view?.findViewById(R.id.addGoalBtn)
        goalListView = view?.findViewById(R.id.goal_recycler_view)

        activity?.let {
            localUser = arguments?.getSerializable(Consts.LOCAL_USER_DATA) as LocalUserData
            welcomeBackTextView?.text = getString(R.string.welcome_back_label, localUser?.userDisplayName)

            userGoals = arguments?.getSerializable(Consts.USER_GOALS) as ArrayList<GoalDataModel>

            adapter = userGoals?.let{ it1 -> GoalRecyclerViewAdapter(it, it1, this) }

            layoutManager = LinearLayoutManager(it)
        }

        goalListView?.adapter = adapter
        goalListView?.layoutManager = layoutManager

        addGoalBtn?.setOnClickListener { goToCreationUI() }
    }

    private fun goToCreationUI()
    {
        val goalCreationBundle = Bundle()
        goalCreationBundle.putSerializable(Consts.LOCAL_USER_DATA, localUser)

        val goalCreationFragment = GoalCreationFragment()
        goalCreationFragment.arguments = goalCreationBundle

        activity?.let {
            it.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, goalCreationFragment, "")
                ?.addToBackStack("null")?.commit()
        }
    }

    override fun onItemClick(position: Int) {
        val selectedGoal = userGoals?.get(position)

        val selectedGoalBundle = Bundle()

        selectedGoalBundle.putSerializable(Consts.SELECTED_GOAL, selectedGoal)
        selectedGoalBundle.putSerializable(Consts.LOCAL_USER_DATA, localUser)

        val goalEditorFragment = GoalEditorFragment()
        goalEditorFragment.arguments = selectedGoalBundle

        activity?.let {
            it.supportFragmentManager?.beginTransaction()?.replace(R.id.frameLayout, goalEditorFragment, "")
                ?.addToBackStack("null")?.commit()
        }
    }
}