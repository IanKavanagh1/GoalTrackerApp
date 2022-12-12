package feature_goals

import android.content.Context
import database.GoalDatabaseOpenHelper
import shared.Consts

object GoalManager
{
    // set up database
    private const val dbName = Consts.GOAL_DATABASE + ".db"
    private lateinit var goalDatabaseOpenHelper: GoalDatabaseOpenHelper

    fun setUpDatabase(context: Context)
    {
        // Initialise database
        goalDatabaseOpenHelper = GoalDatabaseOpenHelper(context, dbName, null, 1)
    }

    // Returns true if the create goal query was successful, false otherwise
    fun createGoal(goalType: Int, goalName: String, goalTarget: String, goalCurrent: String, userId: Int) : Boolean
    {
        return goalDatabaseOpenHelper.createGoal(goalType, goalName, goalTarget, goalCurrent, userId)
    }

    // Returns a list of Goals linked to the userId if found, an empty list if not found
    fun fetchGoals(userId: Int) : ArrayList<GoalDataModel>
    {
        return goalDatabaseOpenHelper.fetchGoals(userId)
    }

    // Returns true if the update goal query was successful, false otherwise
    fun updateGoal(goalId: Int, updatedGoalName: String, updatedGoalProgress: String, updatedGoalType: GoalTypes) : Boolean
    {
        return goalDatabaseOpenHelper.updateGoal(goalId, updatedGoalName, updatedGoalProgress, updatedGoalType)
    }

    // deletes the goal with the provided id
    fun removeGoal(goalId: Int)
    {
        goalDatabaseOpenHelper.removeGoal(goalId)
    }
}