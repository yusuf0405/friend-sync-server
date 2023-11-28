package org.joseph.friendsync.repository.subscription

import org.joseph.friendsync.models.subscription.*
import org.joseph.friendsync.util.Response

interface SubscriptionRepository {

    suspend fun fetchSubscriptionCount(userId: Int): Response<SubscriptionCountResponse>

    suspend fun fetchSubscriptionUserIds(userId: Int): Response<SubscriptionIdsResponse>

    suspend fun fetchSubscriptionUsers(userId: Int): Response<SubscriptionUserResponse>

    suspend fun createSubscription(createSubscription: CreateOrCancelSubscription): Response<SubscriptionCountResponse>

    suspend fun cancelSubscription(cancelSubscription: CreateOrCancelSubscription): Response<SubscriptionCountResponse>
}