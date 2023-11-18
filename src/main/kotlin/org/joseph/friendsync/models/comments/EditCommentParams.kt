package org.joseph.friendsync.models.comments

import kotlinx.serialization.Serializable

@Serializable
data class EditCommentParams(
    val commentId: Int,
    val editedText: String
)