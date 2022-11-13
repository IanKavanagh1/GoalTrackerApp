package com.example.goal_tracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import app_navigation.StartUpFragment
import com.example.goal_tracker.databinding.ActivityMainBinding
import exercise_planner.ExerciseMainFragment
import goal_creation.GoalCreationFragment
import meal_prep.MealMainFragment
import settings.SettingsFragment

class MainActivity : AppCompatActivity()
{
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        replaceFragment(StartUpFragment())

        binding?.bottomNavigationView?.setOnNavigationItemSelectedListener {

            when(it.itemId)
            {
                //TODO: Replace with GoalManagementFragment
                R.id.home -> replaceFragment(GoalCreationFragment())
                R.id.meals -> replaceFragment(MealMainFragment())
                R.id.exercise -> replaceFragment(ExerciseMainFragment())
                R.id.settings -> replaceFragment(SettingsFragment())
            }

            true
        }
    }

    fun replaceFragment(fragment: Fragment)
    {
       var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}