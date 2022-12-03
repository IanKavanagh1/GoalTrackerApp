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
    private var binding: ActivityMainBinding? = null
    private var goalManager: GoalManager? = null

    private val goalManagementFragment = GoalManagementFragment()
    private val goalBundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        goalManager = GoalManager(this)

        //Check if user has any goals
        val sharedPreferences = getSharedPreferences(Consts.USER_PREFS, MODE_PRIVATE)
        val userId = sharedPreferences.getInt(Consts.PREFS_USER_ID, -1)

        val userGoals = goalManager?.fetchGoals(userId)

        //if they do, bring them to the manage fragment
        if(userGoals?.isNotEmpty() == true)
        {
            Log.d("Main Act","User Has Goals")
            goalBundle.putSerializable(Consts.USER_GOALS, userGoals)
            goalManagementFragment.arguments = goalBundle
            replaceFragment(goalManagementFragment)
        }
        //otherwise bring them to the creation fragment
        else
        {
            Log.d("Main Act","User Has No Goals")
            replaceFragment(GoalCreationFragment())
        }

        binding?.bottomNavigationView?.setOnNavigationItemSelectedListener {

            when(it.itemId)
            {
                R.id.home ->
                {
                    goalManagementFragment.arguments = goalBundle
                    replaceFragment(goalManagementFragment)
                }

                R.id.meals -> replaceFragment(MealMainFragment())
                R.id.exercise -> replaceFragment(ExerciseMainFragment())
                R.id.settings -> replaceFragment(SettingsFragment())
            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment)
    {
       val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit()
    }
}