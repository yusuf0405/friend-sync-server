package com.joseph.repository.subscription

import com.joseph.models.subscription.CreateOrCancelSubscription
import com.joseph.models.subscription.SubscriptionCountResponse
import com.joseph.models.subscription.SubscriptionIdsResponse
import com.joseph.models.subscription.SubscriptionUserResponse
import com.joseph.util.Response

interface SubscriptionRepository {

    suspend fun fetchSubscriptionCount(userId: Int): Response<SubscriptionCountResponse>

    suspend fun fetchSubscriptionUserIds(userId: Int): Response<SubscriptionIdsResponse>

    suspend fun fetchSubscriptionUsers(userId: Int): Response<SubscriptionUserResponse>

    suspend fun createSubscription(createSubscription: CreateOrCancelSubscription): Response<SubscriptionCountResponse>

    suspend fun cancelSubscription(cancelSubscription: CreateOrCancelSubscription): Response<SubscriptionCountResponse>
}