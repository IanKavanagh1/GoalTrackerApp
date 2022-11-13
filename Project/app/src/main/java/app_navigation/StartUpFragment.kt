package app_navigation

import account_creation.LoginActivity
import account_creation.RegisterActivity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.goal_tracker.R
import com.example.goal_tracker.databinding.FragmentStartUpBinding

class StartUpFragment : Fragment()
{
    private var _loginButton: Button? = null
    private var _registerButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        val binding = FragmentStartUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        _loginButton = view?.findViewById(R.id.login_button)
        _registerButton = view?.findViewById(R.id.register_button)

        _loginButton?.setOnClickListener {
            startLoginActivity()
            Log.d("Logging App", "Login Button Clicked")
        }

        _registerButton?.setOnClickListener {
            startRegisterActivity()
            Log.d("Logging App", "Register Button Clicked")
        }

    }

    private fun startLoginActivity()
    {
        activity?.let {
            val intent = Intent(it, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startRegisterActivity()
    {
        activity?.let {
            val intent = Intent(it, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}