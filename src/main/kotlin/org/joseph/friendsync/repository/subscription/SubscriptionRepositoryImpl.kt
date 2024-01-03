package org.joseph.friendsync.repository.subscription

import org.joseph.friendsync.db.dao.subscription.SubscriptionDao
import org.joseph.friendsync.models.subscription.*
import org.joseph.friendsync.util.Response
import io.ktor.http.*
import org.joseph.friendsync.models.subscription.ShouldUserSubscriptionResponse
import java.util.concurrent.CancellationException

val something_went_wrong = "Something went wrong!"

class SubscriptionRepositoryImpl(
    private val subscriptionDao: SubscriptionDao,
) : SubscriptionRepository {

    override suspend fun fetchSubscriptionCount(userId: Int): Response<SubscriptionIdResponse> {
        return try {
            Response.Success(
                data = SubscriptionIdResponse(
                    data = ResultSubscriptionId(subscriptionDao.fetchSubscriptionCount(userId))
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = SubscriptionIdResponse(
                    errorMessage = something_went_wrong
                )
            )
        }
    }

    override suspend fun fetchSubscriptionUserIds(userId: Int): Response<SubscriptionIdsResponse> {
        return try {
            Response.Success(
                data = SubscriptionIdsResponse(
                    data = SubscriptionUserIds(subscriptionDao.fetchSubscriptionUserIds(userId))
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = SubscriptionIdsResponse(
                    errorMessage = e.message
                )
            )
        }
    }

    override suspend fun fetchUserSubscriptions(userId: Int): Response<SubscriptionsResponse> {
        return try {
            Response.Success(
                data = SubscriptionsResponse(
                    data = subscriptionDao.fetchUserSubscriptions(userId)
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = SubscriptionsResponse(
                    errorMessage = e.message
                )
            )
        }
    }

    override suspend fun hasUserSubscription(
        userId: Int,
        followingUserId: Int
    ): Response<ShouldUserSubscriptionResponse> {
        return try {
            val isSubscribe = subscriptionDao.hasUserSubscription(userId, followingUserId)
            Response.Success(
                data = ShouldUserSubscriptionResponse(
                    data = isSubscribe
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = ShouldUserSubscriptionResponse(
                    errorMessage = e.message
                )
            )
        }
    }

    override suspend fun fetchSubscriptionUsers(userId: Int): Response<SubscriptionUserResponse> {
        return try {
            Response.Success(
                data = SubscriptionUserResponse(
                    data = subscriptionDao.fetchSubscriptionUsers(userId)
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = SubscriptionUserResponse(
                    errorMessage = e.message
                )
            )
        }
    }

    override suspend fun createSubscription(createSubscription: CreateOrCancelSubscription): Response<SubscriptionIdResponse> {
        return try {
            Response.Success(
                data = SubscriptionIdResponse(
                    data = ResultSubscriptionId(subscriptionDao.createSubscription(createSubscription))
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = SubscriptionIdResponse(
                    errorMessage = something_went_wrong
                )
            )
        }
    }

    override suspend fun cancelSubscription(cancelSubscription: CreateOrCancelSubscription): Response<SubscriptionIdResponse> {
        return try {
            Response.Success(
                data = SubscriptionIdResponse(
                    data = ResultSubscriptionId(subscriptionDao.cancelSubscription(cancelSubscription))
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = SubscriptionIdResponse(
                    errorMessage = something_went_wrong
                )
            )
        }
    }
}