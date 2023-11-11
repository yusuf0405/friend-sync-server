package org.joseph.friendsync.db.dao.post

import com.joseph.models.post.AddPostParams
import com.joseph.models.post.Post

interface PostDao {

    suspend fun addPost(params: AddPostParams): Post?

    suspend fun fetchPostById(postId: Int): Post?

    suspend fun fetchUserPosts(userId: Int): List<Post>

    suspend fun fetchUserRecommendedPosts(
        page: Int,
        pageSize: Int,
        followedUserIds: List<Int>
    ): List<Post>

    suspend fun searchPostsWithParams(
        page: Int,
        pageSize: Int,
        query: String
    ): List<Post>
}