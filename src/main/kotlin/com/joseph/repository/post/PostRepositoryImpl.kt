package com.joseph.repository.post

import com.joseph.db.dao.post.PostDao
import com.joseph.db.dao.subscription.SubscriptionDao
import com.joseph.models.post.AddPostParams
import com.joseph.models.post.PostListResponse
import com.joseph.models.post.PostResponse
import com.joseph.models.post.PostWithPagingParam
import com.joseph.repository.subscription.SubscriptionRepository
import com.joseph.util.Response
import io.ktor.http.*
import org.postgresql.util.PSQLException
import java.util.concurrent.CancellationException

class PostRepositoryImpl(
    private val postDao: PostDao,
    private val subscriptionDao: SubscriptionDao
) : PostRepository {
    override suspend fun addPost(addPost: AddPostParams): Response<PostResponse> {
        return try {
            val post = postDao.addPost(addPost)
            if (post == null) {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = PostResponse(
                        errorMessage = "Oops, sorry, we couldn't add a post, try again later!"
                    )
                )
            } else {
                Response.Success(
                    data = PostResponse(
                        data = post,
                        errorMessage = ""
                    )
                )
            }

        } catch (e: PSQLException) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostResponse(
                    errorMessage = "There is no such user!"
                )
            )
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostResponse(
                    errorMessage = e.message
                )
            )
        }
    }

    override suspend fun fetchUserPosts(userId: Int): Response<PostListResponse> {
        return try {
            Response.Success(
                data = PostListResponse(
                    data = postDao.fetchUserPosts(userId)
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostListResponse(
                    errorMessage = e.message
                )
            )
        }
    }

    override suspend fun fetchUserRecommendedPosts(param: PostWithPagingParam): Response<PostListResponse> {
        return try {
            val followedUserIds = subscriptionDao.fetchSubscriptionUserIds(param.userId)
            Response.Success(
                data = PostListResponse(
                    data = postDao.fetchUserRecommendedPosts(
                        page = param.page,
                        pageSize = param.pageSize,
                        followedUserIds = followedUserIds
                    )
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostListResponse(
                    errorMessage = e.message
                )
            )
        }
    }
}