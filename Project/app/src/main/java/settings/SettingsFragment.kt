package settings

import account_creation.LoginActivity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import app_preferences.UserPreferenceManager
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment()
{
    private var logOutButton: Button? = null

    private val USER_PREFS = "user_prefs"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        logOutButton = view?.findViewById(R.id.logOutBtn)

        logOutButton?.setOnClickListener { logOut() }
    }

    private fun logOut()
    {
        //UserPreferenceManager.flagUserAsLoggedOut()

        activity?.let {
            var sharedPreferences = it.getSharedPreferences(USER_PREFS, AppCompatActivity.MODE_PRIVATE)
            var editor = sharedPreferences.edit()

            editor.apply {
                putBoolean("loggedIn", false)
            }.apply()

            var intent = Intent(it, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}