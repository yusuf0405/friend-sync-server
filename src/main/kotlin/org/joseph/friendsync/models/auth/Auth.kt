package org.joseph.friendsync.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class SignUpParams(
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
)

@Serializable
data class SignInParams(
    val email: String,
    val password: String,
)

@Serializable
data class AuthResponse(
    val data: AuthResponseData? = null,
    val errorMessage: String? = null
)

@Serializable
data class AuthResponseData(
    val id: Int,
    val name: String,
    val lastName: String,
    val bio: String?,
    val avatar: String? = null,
    val token: String,
)