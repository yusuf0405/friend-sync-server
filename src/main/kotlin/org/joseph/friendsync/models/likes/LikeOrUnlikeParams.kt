package org.joseph.friendsync.models.likes

import kotlinx.serialization.Serializable

@Serializable
data class LikeOrUnlikeParams(
    val userId: Int,
    val postId: Int,
)