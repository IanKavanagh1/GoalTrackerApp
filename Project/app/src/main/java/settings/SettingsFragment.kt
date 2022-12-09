package settings

import account_creation.LocalUserData
import account_creation.LoginActivity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentSettingsBinding
import shared.Consts

class SettingsFragment : Fragment()
{
    // UI Variables
    private lateinit var logOutButton: Button
    private lateinit var emailTextView: TextView
    private lateinit var displayNameTextView: TextView
    private lateinit var passwordTextView: TextView

    // Edit Buttons
    private lateinit var editEmailButton: ImageButton
    private lateinit var editDisplayNameButton: ImageButton
    private lateinit var editPasswordButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        emailTextView = view!!.findViewById(R.id.loggedInUserTextView)
        displayNameTextView = view!!.findViewById(R.id.loggedInUserDisplayName)
        passwordTextView = view!!.findViewById(R.id.loggedInUserPassword)

        // grab user data from the passed arguments
        val localUserData = arguments?.getSerializable(Consts.LOCAL_USER_DATA) as LocalUserData

        // display user data
        emailTextView.text = getString(R.string.user_email_text, localUserData.userEmail)
        displayNameTextView.text = getString(R.string.user_email_text, localUserData.userDisplayName)
        passwordTextView.text = getString(R.string.user_email_text, localUserData.userPassword)

        // set up listener for logout button
        logOutButton = view!!.findViewById(R.id.logOutBtn)
        logOutButton.setOnClickListener { logOut() }

        // Grab edit buttons and set up listeners
        editEmailButton = view!!.findViewById(R.id.editEmailButton)
        editEmailButton.setOnClickListener { editEmail() }

        editDisplayNameButton = view!!.findViewById(R.id.editDisplayNameButton)
        editDisplayNameButton.setOnClickListener { editDisplayName() }

        editPasswordButton = view!!.findViewById(R.id.editPasswordButton)
        editPasswordButton.setOnClickListener { editPassword() }
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

    private fun editEmail()
    {
        Log.d("Settings Frag","Edit Email")
    }

    private fun editDisplayName()
    {
        Log.d("Settings Frag","Edit Display Name")
    }

    private fun editPassword()
    {
        Log.d("Settings Frag","Edit Password")
    }
}