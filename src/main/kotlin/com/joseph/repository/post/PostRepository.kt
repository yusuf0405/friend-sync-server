package com.joseph.repository.post

import com.joseph.models.post.*
import com.joseph.util.Response

interface PostRepository {
    suspend fun addPost(post: AddPostParams): Response<PostResponse>

    suspend fun fetchUserPosts(userId: Int): Response<PostListResponse>

    suspend fun fetchUserRecommendedPosts(
        param: PostWithPagingParam
    ): Response<PostListResponse>
}