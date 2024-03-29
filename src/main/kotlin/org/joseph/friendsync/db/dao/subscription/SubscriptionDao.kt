package org.joseph.friendsync.db.dao.subscription

import org.joseph.friendsync.models.subscription.CreateOrCancelSubscription
import org.joseph.friendsync.models.subscription.Subscription
import org.joseph.friendsync.models.subscription.SubscriptionResultUser

interface SubscriptionDao {

    suspend fun fetchSubscriptionCount(userId: Int): Int

    suspend fun fetchFollowingCount(userId: Int): Int

    suspend fun hasUserSubscription(userId: Int, followingUserId: Int): Boolean

    suspend fun fetchSubscriptionUserIds(userId: Int): List<Int>

    suspend fun fetchUserSubscriptions(userId: Int): List<Subscription>

    suspend fun fetchSubscriptionUsers(userId: Int): List<SubscriptionResultUser>

    suspend fun createSubscription(createSubscription: CreateOrCancelSubscription): Int

    suspend fun cancelSubscription(cancelSubscription: CreateOrCancelSubscription): Int

}