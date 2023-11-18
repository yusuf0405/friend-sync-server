package org.joseph.friendsync.models.post

import kotlinx.serialization.Serializable
import org.joseph.friendsync.models.user.UserInfo

@Serializable
data class AddPostParams(
    val userId: Int,
    val message: String?,
    val imageUrls: List<String>,
)

@Serializable
data class PostResponse(
    val data: Post? = null,
    val errorMessage: String? = null
)

@Serializable
data class PostListResponse(
    val data: List<Post>? = null,
    val errorMessage: String? = null
)

@Serializable
data class Post(
    val id: Int,
    val user: UserInfo,
    val message: String?,
    val imageUrls: List<String>,
    val releaseDate: Long,
    val likesCount: Int,
    val commentsCount: Int,
    val savedCount: Int,
)






