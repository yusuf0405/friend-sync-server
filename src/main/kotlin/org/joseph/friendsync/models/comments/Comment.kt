package org.joseph.friendsync.models.comments

import kotlinx.serialization.Serializable
import org.joseph.friendsync.models.user.UserInfo

@Serializable
data class Comment(
    val id: Int,
    val comment: String,
    val postId: Int,
    val likesCount: Int,
    val user: UserInfo,
    val releaseDate: Long,
)

@Serializable
data class CommentResponse(
    val data: Comment? = null,
    val errorMessage: String? = null
)

@Serializable
data class CommentListResponse(
    val data: List<Comment>? = null,
    val errorMessage: String? = null
)