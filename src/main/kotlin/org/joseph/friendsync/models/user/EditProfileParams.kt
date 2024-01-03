package org.joseph.friendsync.models.user

import kotlinx.serialization.Serializable

@Serializable
data class EditProfileParamsResponse(
    val data: EditProfileParams? = null,
    val errorMessage: String? = null
)

@Serializable
data class EditProfileParams(
    val id: Int,
    val name: String,
    val lastName: String,
    val email: String,
    val bio: String,
    val education: String,
    val avatar: String,
)