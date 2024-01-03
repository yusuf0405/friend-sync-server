package org.joseph.friendsync.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserPersonalInfoResponse(
    val data: UserPersonalInfo? = null,
    val errorMessage: String? = null
)

@Serializable
data class UserPersonalInfo(
    val id: Int,
    val email: String,
) {
    companion object {
        val unknown = UserPersonalInfo(
            id = -1,
            email = String(),
        )
    }
}