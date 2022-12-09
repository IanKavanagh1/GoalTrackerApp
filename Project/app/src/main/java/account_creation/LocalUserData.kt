package account_creation

import java.io.Serializable

// Data Class to store data around the local user
// This is used to help share user data between activities and fragments
data class LocalUserData(val userId: Int,
                         val userEmail: String,
                         val userDisplayName: String,
                         val userPassword: String
                         ) : Serializable
