package org.joseph.friendsync.models.subscription

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionsResponse(
    val data: List<Subscription>? = null,
    val errorMessage: String? = null
)


@Serializable
data class Subscription(
    val id: Int,
    val followerId: Int,
    val followingId: Int
)