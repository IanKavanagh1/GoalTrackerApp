package feature_goals

import java.io.Serializable

// Data class to hold all goal data
data class GoalDataModel(
    val goalId: Int,
    val goalType: Int,
    val goalName: String?,
    val goalProgress: Float) : Serializable