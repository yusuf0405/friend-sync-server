package com.joseph.repository.post

import com.joseph.db.dao.post.PostDao
import com.joseph.db.dao.subscription.SubscriptionDao
import com.joseph.models.post.*
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

    override suspend fun fetchUserRecommendedPosts(
        page: Int,
        pageSize: Int,
        userId: Int
    ): Response<PostListResponse> {
        return try {
            val followedUserIds = subscriptionDao.fetchSubscriptionUserIds(userId)
            Response.Success(
                data = PostListResponse(
                    data = postDao.fetchUserRecommendedPosts(
                        page = page,
                        pageSize = pageSize,
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

    override suspend fun searchPostsWithParams(
        page: Int, pageSize: Int,
        query: String
    ): Response<PostListResponse> {
        val data = postDao.searchPostsWithParams(
            page = page,
            pageSize = pageSize,
            query = query
        )
        return try {
            Response.Success(data = PostListResponse(data = data))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = PostListResponse(errorMessage = e.message)
            )
        }
    }
}