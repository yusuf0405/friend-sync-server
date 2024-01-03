package org.joseph.friendsync.db.dao.likes

import org.joseph.friendsync.models.likes.LikedPost

interface LikesPostDao {

    suspend fun likePost(userId: Int, postId: Int): LikedPost?

    suspend fun unlikePost(userId: Int, postId: Int): LikedPost?

    suspend fun fetchPostLikedCount(postId: Int): Int

    suspend fun fetchLikedPostsCount(userId: Int): List<LikedPost>

    suspend fun fetchUserLikedPostIds(postId: Int): List<Int>

    suspend fun fetchPostLikeUserIds(postId: Int): List<Int>
}