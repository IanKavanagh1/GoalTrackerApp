package settings

import account_creation.LocalUserData
import account_creation.LoginActivity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentSettingsBinding
import shared.Consts

class SettingsFragment : Fragment()
{
    // UI Variables
    private var logOutButton: Button? = null
    private var emailTextView: TextView? = null
    private var displayNameTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        emailTextView = view?.findViewById(R.id.loggedInUserTextView)
        displayNameTextView = view?.findViewById(R.id.loggedInUserDisplayName)

        // grab user data from the passed arguments
        val localUserData = arguments?.getSerializable(Consts.LOCAL_USER_DATA) as LocalUserData

        // display user data
        emailTextView?.text = getString(R.string.user_email_text, localUserData.userEmail)
        displayNameTextView?.text = getString(R.string.user_email_text, localUserData.userDisplayName)

        // set up listener for logout button
        logOutButton = view?.findViewById(R.id.logOutBtn)
        logOutButton?.setOnClickListener { logOut() }
    }

    private fun logOut()
    {
        activity?.let {

            // grab the shared prefs
            val sharedPreferences = it.getSharedPreferences(Consts.USER_PREFS, AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.apply {
                // flag the user as logged out
                putBoolean(Consts.PREFS_LOGGED_IN, false)
            }.apply()

            // bring user back to login act
            val intent = Intent(it, LoginActivity::class.java)
            startActivity(intent)

            // close main activity
            it.finish()
        }
    }
}