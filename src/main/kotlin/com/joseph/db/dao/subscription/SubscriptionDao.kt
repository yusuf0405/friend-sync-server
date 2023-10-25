package com.joseph.db.dao.subscription

import com.joseph.models.subscription.CreateOrCancelSubscription
import com.joseph.models.subscription.SubscriptionResultUser

interface SubscriptionDao {

    suspend fun fetchSubscriptionCount(userId: Int): Int

    suspend fun fetchFollowingCount(userId: Int): Int

    suspend fun fetchSubscriptionUserIds(userId: Int): List<Int>

    suspend fun fetchSubscriptionUsers(userId: Int): List<SubscriptionResultUser>

    suspend fun createSubscription(createSubscription: CreateOrCancelSubscription): Int

    suspend fun cancelSubscription(cancelSubscription: CreateOrCancelSubscription): Int

}