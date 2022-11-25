package feature_goals

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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

    private val USER_PREFS = "user_prefs"

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

            var sharedPreferences = it.getSharedPreferences(USER_PREFS, AppCompatActivity.MODE_PRIVATE)
            var userId = sharedPreferences.getInt("userId", -1)

            Log.d("GoalManagement","Local User Id is $userId")

            var userGoals = goalManager?.fetchGoals(userId)

            adapter = userGoals?.let{ it1 -> GoalRecyclerViewAdapter(it, it1) }

            layoutManager = LinearLayoutManager(it)
        }

        goalListView = view?.findViewById(R.id.goal_recycler_view)

        goalListView?.adapter = adapter
        goalListView?.layoutManager = layoutManager
    }
}