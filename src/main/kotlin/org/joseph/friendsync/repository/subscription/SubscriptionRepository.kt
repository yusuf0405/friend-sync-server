package org.joseph.friendsync.repository.subscription

import org.joseph.friendsync.models.subscription.*
import org.joseph.friendsync.models.subscription.ShouldUserSubscriptionResponse
import org.joseph.friendsync.util.Response

interface SubscriptionRepository {

    suspend fun fetchSubscriptionCount(userId: Int): Response<SubscriptionIdResponse>

    suspend fun fetchSubscriptionUserIds(userId: Int): Response<SubscriptionIdsResponse>

    suspend fun fetchUserSubscriptions(userId: Int): Response<SubscriptionsResponse>

    suspend fun hasUserSubscription(userId: Int, followingUserId: Int): Response<ShouldUserSubscriptionResponse>

    suspend fun fetchSubscriptionUsers(userId: Int): Response<SubscriptionUserResponse>

    suspend fun createSubscription(createSubscription: CreateOrCancelSubscription): Response<SubscriptionIdResponse>

    suspend fun cancelSubscription(cancelSubscription: CreateOrCancelSubscription): Response<SubscriptionIdResponse>
}