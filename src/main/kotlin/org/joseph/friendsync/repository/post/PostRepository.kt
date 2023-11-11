package org.joseph.friendsync.repository.post

import com.joseph.models.post.*
import com.joseph.util.Response

interface PostRepository {
    suspend fun addPost(params: AddPostParams): Response<PostResponse>

    suspend fun fetchUserPosts(userId: Int): Response<PostListResponse>

    suspend fun fetchPostById(postId: Int): Response<PostResponse>

    suspend fun fetchUserRecommendedPosts(
        page: Int,
        pageSize: Int,
        userId: Int
    ): Response<PostListResponse>

    suspend fun searchPostsWithParams(
        page: Int,
        pageSize: Int,
        query: String
    ): Response<PostListResponse>
}