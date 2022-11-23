package feature_goals

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentGoalManagmentBinding

class GoalManagementFragment : Fragment()
{
    private var goalManager: GoalManager? = null
    private var goalListView: RecyclerView? = null
    private var adapter: GoalRecyclerViewAdapter? = null
    private var layoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGoalManagmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activity?.let {
            goalManager = GoalManager(it)

            //TODO: Use Actual UserId From AccountManager
            var userGoals = goalManager?.fetchGoals(0)

            adapter = userGoals?.let{ it1 -> GoalRecyclerViewAdapter(it, it1) }

            layoutManager = LinearLayoutManager(it)
        }

        goalListView = view?.findViewById(R.id.goal_recycler_view)

        goalListView?.adapter = adapter
        goalListView?.layoutManager = layoutManager
    }
}