package com.joseph.repository.subscription

import com.joseph.db.dao.subscription.SubscriptionDao
import com.joseph.models.subscription.*
import com.joseph.util.Response
import io.ktor.http.*
import java.util.concurrent.CancellationException

val something_went_wrong = "Something went wrong!"

class SubscriptionRepositoryImpl(
    private val subscriptionDao: SubscriptionDao,
) : SubscriptionRepository {

    override suspend fun fetchSubscriptionCount(userId: Int): Response<SubscriptionCountResponse> {
        return try {
            Response.Success(
                data = SubscriptionCountResponse(
                    data = ResultSubscriptionCount(subscriptionDao.fetchSubscriptionCount(userId))
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = SubscriptionCountResponse(
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

    override suspend fun createSubscription(createSubscription: CreateOrCancelSubscription): Response<SubscriptionCountResponse> {
        return try {
            Response.Success(
                data = SubscriptionCountResponse(
                    data = ResultSubscriptionCount(subscriptionDao.createSubscription(createSubscription))
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = SubscriptionCountResponse(
                    errorMessage = something_went_wrong
                )
            )
        }
    }

    override suspend fun cancelSubscription(cancelSubscription: CreateOrCancelSubscription): Response<SubscriptionCountResponse> {
        return try {
            Response.Success(
                data = SubscriptionCountResponse(
                    data = ResultSubscriptionCount(subscriptionDao.cancelSubscription(cancelSubscription))
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = SubscriptionCountResponse(
                    errorMessage = something_went_wrong
                )
            )
        }
    }
}