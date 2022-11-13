package settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }
}