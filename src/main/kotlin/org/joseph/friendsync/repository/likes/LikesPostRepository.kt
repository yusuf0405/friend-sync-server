package org.joseph.friendsync.repository.likes

import org.joseph.friendsync.models.likes.LikedListIdsResponse
import org.joseph.friendsync.models.likes.LikedPostListResponse
import org.joseph.friendsync.models.likes.LikedPostResponse
import org.joseph.friendsync.util.Response

interface LikesPostRepository {

    suspend fun likePost(userId: Int, postId: Int): Response<LikedPostResponse>

    suspend fun unlikePost(userId: Int, postId: Int): Response<Unit>

    suspend fun fetchPostLikedCount(postId: Int): Response<Int>

    suspend fun fetchLikedPosts(postId: Int): Response<LikedPostListResponse>

    suspend fun fetchUserLikedPostIds(postId: Int): Response<LikedListIdsResponse>

    suspend fun fetchPostLikeUserIds(postId: Int): Response<LikedListIdsResponse>
}