package account_creation

import java.io.Serializable

data class LocalUserData(val userId: Int, val userEmail: String, val userDisplayName: String) : Serializable
