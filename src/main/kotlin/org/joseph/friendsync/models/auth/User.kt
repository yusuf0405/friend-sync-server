package org.joseph.friendsync.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val lastName: String,
    val bio: String?,
    val avatar: String?,
    val password: String,
    val email: String,
    val profileBackground: String?,
    val education: String?,
    val releaseDate: Long,
)



