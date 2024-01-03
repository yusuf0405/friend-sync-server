package org.joseph.friendsync.models.likes

import kotlinx.serialization.Serializable

@Serializable
data class LikedPostListResponse(
    val data: List<LikedPost>? = null,
    val errorMessage: String? = null
)

@Serializable
data class LikedPostResponse(
    val data: LikedPost? = null,
    val errorMessage: String? = null
)

@Serializable
data class LikedPost(
    val id: Int,
    val userId: Int,
    val postId: Int,
)