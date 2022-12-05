package shared

// Static object to store all constant values used throughout the app
// so that we have only one place to update if we need to
object Consts
{
    // User Preferences
    const val USER_PREFS = "user_prefs"
    const val PREFS_USER_ID = "userId"
    const val PREFS_USER_DISPLAY_NAME = "userDisplayName"
    const val PREFS_LOGGED_IN = "loggedIn"

    // Databases
    const val USER_DATABASE = "user_database"
    const val GOAL_DATABASE = "goal_database"
    const val HEART_RATE_DATABASE = "hear_rate_database"
    const val EXERCISE_DATABASE = "exercise_database"

    // Fragment Arguments
    const val USER_GOALS = "user_goals"
    const val SELECTED_GOAL = "selected_goal"

    // Activity Arguments
    const val LOCAL_USER_DATA = "local_user_data"
}