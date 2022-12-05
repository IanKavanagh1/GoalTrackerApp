package exercise_planner

// Data class to store exercise data
data class ExerciseDataModel(
    val startLat: Double,
    val startLon: Double,
    val endLat: Double,
    val endLon: Double,
    val distanceCovered: Double,
    val timeTaken: Long
)
