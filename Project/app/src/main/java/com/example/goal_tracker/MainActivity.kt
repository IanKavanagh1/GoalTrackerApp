package com.example.goal_tracker

import account_creation.LocalUserData
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.goal_tracker.databinding.ActivityMainBinding
import exercise_planner.ExerciseMainFragment
import feature_goals.GoalCreationFragment
import feature_goals.GoalDataModel
import feature_goals.GoalManagementFragment
import feature_goals.GoalManager
import settings.SettingsFragment
import shared.Consts

class MainActivity : AppCompatActivity()
{
    // Variables for act context and bundles
    private lateinit var binding: ActivityMainBinding

    private val goalManagementFragment = GoalManagementFragment()
    private val goalBundle = Bundle()
    private var localUserData: LocalUserData? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Initialize the Goal database
        GoalManager.setUpDatabase(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get the user data provided by the login or register act
        localUserData = intent.getSerializableExtra(Consts.LOCAL_USER_DATA) as LocalUserData?

        // get the user goals
        val userGoals = getUserGoals(localUserData!!.userId)

        //if the goals list is not empty, bring them to the manage fragment
        if(userGoals.isNotEmpty())
        {
            // store the user goals into the goal bundle
            goalBundle.putSerializable(Consts.USER_GOALS, userGoals)

            // store the local user data into the goal bundle
            goalBundle.putSerializable(Consts.LOCAL_USER_DATA, localUserData)

            // pass the goal bundle to the goal management frag so it can use the data
            goalManagementFragment.arguments = goalBundle

            // display the goal management fragment
            replaceFragment(goalManagementFragment)
        }
        //otherwise bring them to the creation fragment
        else
        {
            // create a goal creation bundle to shared data to the GoalCreationFragment
            val goalCreationBundle = Bundle()

            // share the local user data
            goalCreationBundle.putSerializable(Consts.LOCAL_USER_DATA, localUserData)

            val goalCreationFragment = GoalCreationFragment()

            // pass the bundle to the creation fragment
            goalCreationFragment.arguments = goalCreationBundle

            // display the goal creation fragment
            replaceFragment(goalCreationFragment)
        }

        // set up the bottom nav menu listeners
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {

            // switch statement to update the fragment displayed based on the selected menu item
            when(it.itemId)
            {
                R.id.home ->
                {
                    // shared user data and goals with the goal management fragment
                    goalBundle.putSerializable(Consts.USER_GOALS, getUserGoals(localUserData!!.userId))
                    goalBundle.putSerializable(Consts.LOCAL_USER_DATA, localUserData)
                    goalManagementFragment.arguments = goalBundle
                    replaceFragment(goalManagementFragment)
                }

                R.id.exercise -> replaceFragment(ExerciseMainFragment())
                R.id.settings -> {

                    val settingsFragment = SettingsFragment()
                    val settingsBundle = Bundle()

                    // shared the local user data with the settings fragment
                    settingsBundle.putSerializable(Consts.LOCAL_USER_DATA, localUserData)
                    settingsFragment.arguments = settingsBundle
                    replaceFragment(settingsFragment)
                }
            }

            true
        }
    }

    // Method to update the displayed fragment
    private fun replaceFragment(fragment: Fragment)
    {
       val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit()
    }

    // returns the user goals for the provided user id, an empty list if they don't have any
    private fun getUserGoals(userId: Int) : ArrayList<GoalDataModel>
    {
       return GoalManager.fetchGoals(userId)
    }
}