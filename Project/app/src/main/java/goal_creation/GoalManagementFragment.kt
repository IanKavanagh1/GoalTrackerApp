package goal_creation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goal_tracker.databinding.FragmentGoalManagmentBinding

class GoalManagementFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGoalManagmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}