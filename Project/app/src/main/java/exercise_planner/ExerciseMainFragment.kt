package exercise_planner

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentExerciseMainBinding

class ExerciseMainFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = FragmentExerciseMainBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}