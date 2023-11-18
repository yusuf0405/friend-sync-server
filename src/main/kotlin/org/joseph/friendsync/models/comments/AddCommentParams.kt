package org.joseph.friendsync.models.comments

import kotlinx.serialization.Serializable

@Serializable
data class AddCommentParams(
    val userId: Int,
    val postId: Int,
    val commentText: String
)