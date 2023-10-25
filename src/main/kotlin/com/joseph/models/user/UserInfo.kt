package com.joseph.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserListResponse(
    val data: List<UserInfo>? = null,
    val errorMessage: String? = null
)
@Serializable
data class UserInfo(
    val id: Int,
    val name: String,
    val lastName: String,
    val avatar: String?,
    val releaseDate: Long,
)