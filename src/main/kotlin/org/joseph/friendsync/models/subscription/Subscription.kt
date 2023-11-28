package org.joseph.friendsync.models.subscription

import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
    val id: Int,
    val userId: Int,
    val followedUserIds: List<Int>
)