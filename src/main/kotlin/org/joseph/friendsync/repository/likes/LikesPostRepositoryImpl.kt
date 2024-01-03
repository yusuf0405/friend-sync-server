package org.joseph.friendsync.repository.likes

import io.ktor.http.*
import org.joseph.friendsync.db.dao.likes.LikesPostDao
import org.joseph.friendsync.models.likes.LikedListIds
import org.joseph.friendsync.models.likes.LikedListIdsResponse
import org.joseph.friendsync.models.likes.LikedPostListResponse
import org.joseph.friendsync.models.likes.LikedPostResponse
import org.joseph.friendsync.repository.subscription.something_went_wrong
import org.joseph.friendsync.util.Response
import org.joseph.friendsync.util.extensions.callSafe

class LikesPostRepositoryImpl(
    private val likesPostDao: LikesPostDao
) : LikesPostRepository {
    override suspend fun likePost(
        userId: Int,
        postId: Int
    ): Response<LikedPostResponse> = callSafe(
        Response.Error(
            code = HttpStatusCode.InternalServerError,
            data = LikedPostResponse(errorMessage = something_went_wrong)
        )
    ) {
        val result = likesPostDao.likePost(userId, postId)
        if (result == null) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = LikedPostResponse(errorMessage = something_went_wrong)
            )
        } else Response.Success(data = LikedPostResponse(data = result))
    }

    override suspend fun unlikePost(
        userId: Int,
        postId: Int
    ): Response<Unit> = callSafe(
        Response.Error(
            code = HttpStatusCode.InternalServerError,
            data = Unit
        )
    ) {
        val result = likesPostDao.unlikePost(userId, postId)
        Response.Success(data = Unit)
    }

    override suspend fun fetchPostLikedCount(postId: Int): Response<Int> = callSafe(
        Response.Error(
            code = HttpStatusCode.InternalServerError,
            data = -1
        )
    ) {
        val count = likesPostDao.fetchPostLikedCount(postId)
        Response.Success(count)
    }

    override suspend fun fetchLikedPosts(
        postId: Int
    ): Response<LikedPostListResponse> = callSafe(
        Response.Error(
            code = HttpStatusCode.InternalServerError,
            data = LikedPostListResponse(errorMessage = something_went_wrong)
        )
    ) {
        Response.Success(LikedPostListResponse(data = likesPostDao.fetchLikedPostsCount(postId)))
    }

    override suspend fun fetchUserLikedPostIds(
        postId: Int
    ): Response<LikedListIdsResponse> = callSafe(
        Response.Error(
            code = HttpStatusCode.InternalServerError,
            data = LikedListIdsResponse(errorMessage = something_went_wrong)
        )
    ) {
        val ids = likesPostDao.fetchUserLikedPostIds(postId)
        Response.Success(LikedListIdsResponse(data = LikedListIds(ids = ids)))
    }

    override suspend fun fetchPostLikeUserIds(
        postId: Int
    ): Response<LikedListIdsResponse> = callSafe(
        Response.Error(
            code = HttpStatusCode.InternalServerError,
            data = LikedListIdsResponse(errorMessage = something_went_wrong)
        )
    ) {
        val ids = likesPostDao.fetchPostLikeUserIds(postId)
        Response.Success(LikedListIdsResponse(data = LikedListIds(ids = ids)))
    }
}