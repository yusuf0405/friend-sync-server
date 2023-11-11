package org.joseph.friendsync.models.subscription

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrCancelSubscription(
    val followerId: Int,
    val followingId: Int
)