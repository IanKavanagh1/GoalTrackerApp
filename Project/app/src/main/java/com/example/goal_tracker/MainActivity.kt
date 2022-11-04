package com.example.goal_tracker

import account_creation.LoginActivity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.content.Intent

class MainActivity : AppCompatActivity()
{
    private var _loginButton: Button? = null
    private var _registerButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _loginButton = findViewById(R.id.login_button)
        _registerButton = findViewById(R.id.register_button)

        _loginButton?.setOnClickListener {
            startLoginActivity()
            Log.d("Logging App", "Login Button Clicked")
        }

        _registerButton?.setOnClickListener { Log.d("Logging App", "Register Button Clicked") }
    }

    private fun startLoginActivity()
    {
        var intent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}