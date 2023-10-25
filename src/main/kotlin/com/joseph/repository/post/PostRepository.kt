package com.joseph.repository.post

import com.joseph.models.post.AddPostParams
import com.joseph.models.post.PostListResponse
import com.joseph.models.post.PostResponse
import com.joseph.util.Response

interface PostRepository {
    suspend fun addPost(post: AddPostParams): Response<PostResponse>

    suspend fun fetchUserPosts(userId: Int): Response<PostListResponse>
}