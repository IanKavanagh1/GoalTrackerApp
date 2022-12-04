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
    private var binding: ActivityMainBinding? = null

    private val goalManagementFragment = GoalManagementFragment()
    private val goalBundle = Bundle()
    private var localUserData: LocalUserData? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        GoalManager.setUpDatabase(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        localUserData = intent.getSerializableExtra(Consts.LOCAL_USER_DATA) as LocalUserData?

        val userGoals = getUserGoals(localUserData!!.userId)

        //if they do, bring them to the manage fragment
        if(userGoals.isNotEmpty())
        {
            goalBundle.putSerializable(Consts.USER_GOALS, userGoals)
            goalBundle.putSerializable(Consts.LOCAL_USER_DATA, localUserData)
            goalManagementFragment.arguments = goalBundle
            replaceFragment(goalManagementFragment)
        }
        //otherwise bring them to the creation fragment
        else
        {
            val goalCreationBundle = Bundle()
            goalCreationBundle.putSerializable(Consts.LOCAL_USER_DATA, localUserData)

            val goalCreationFragment = GoalCreationFragment()
            goalCreationFragment.arguments = goalCreationBundle

            replaceFragment(goalCreationFragment)
        }

        binding?.bottomNavigationView?.setOnNavigationItemSelectedListener {

            when(it.itemId)
            {
                R.id.home ->
                {
                    goalBundle.putSerializable(Consts.USER_GOALS, getUserGoals(localUserData!!.userId))
                    goalBundle.putSerializable(Consts.LOCAL_USER_DATA, localUserData)
                    goalManagementFragment.arguments = goalBundle
                    replaceFragment(goalManagementFragment)
                }

                R.id.exercise -> replaceFragment(ExerciseMainFragment())
                R.id.settings -> {

                    val settingsFragment = SettingsFragment()
                    val settingsBundle = Bundle()

                    settingsBundle.putSerializable(Consts.LOCAL_USER_DATA, localUserData)
                    settingsFragment.arguments = settingsBundle
                    replaceFragment(settingsFragment)
                }
            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment)
    {
       val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit()
    }

    private fun getUserGoals(userId: Int) : ArrayList<GoalDataModel>
    {
       return GoalManager.fetchGoals(userId)
    }
}