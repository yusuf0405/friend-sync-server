package org.joseph.friendsync.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserDetailResponse(
    val data: UserDetail? = null,
    val errorMessage: String? = null
)

@Serializable
data class UserDetail(
    val id: Int,
    val name: String,
    val lastName: String,
    val bio: String?,
    val avatar: String?,
    val profileBackground: String?,
    val education: String?,
    val releaseDate: Long,
    val followersCount: Int,
    val followingCount: Int
)