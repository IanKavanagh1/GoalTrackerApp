package com.example.goal_tracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.example.goal_tracker.databinding.ActivityMainBinding
import exercise_planner.ExerciseMainFragment
import feature_goals.GoalCreationFragment
import feature_goals.GoalManagementFragment
import feature_goals.GoalManager
import meal_prep.MealMainFragment
import settings.SettingsFragment
import shared.Consts

class MainActivity : AppCompatActivity()
{
    var binding: ActivityMainBinding? = null
    var goalManager: GoalManager? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        goalManager = GoalManager(this)

        binding?.bottomNavigationView?.setOnNavigationItemSelectedListener {

            when(it.itemId)
            {
                R.id.home -> replaceFragment(GoalManagementFragment())
                R.id.meals -> replaceFragment(MealMainFragment())
                R.id.exercise -> replaceFragment(ExerciseMainFragment())
                R.id.settings -> replaceFragment(SettingsFragment())
            }

            true
        }
    }

    override fun onStart() {
        super.onStart()

        //Check if user has any goals
        var sharedPreferences = getSharedPreferences(Consts.USER_PREFS, MODE_PRIVATE)
        var userId = sharedPreferences.getInt(Consts.PREFS_USER_ID, -1)

        var userGoals = goalManager?.fetchGoals(userId)

        //if they do, bring them to the manage fragment
        if(userGoals?.isNotEmpty() == true)
        {
            Log.d("Main Act","User Has Goals")
            replaceFragment(GoalManagementFragment())
        }
        //otherwise bring them to the creation fragment
        else
        {
            Log.d("Main Act","User Has No Goals")
            replaceFragment(GoalCreationFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment)
    {
       var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}