package feature_goals

import java.io.Serializable

data class GoalDataModel(
    val iconId: Int,
    val goalType: Int,
    val goalName: String?,
    val goalProgress: Float) : Serializable