package org.joseph.friendsync.models.subscription

data class Subscription(
    val id: Int,
    val followerId: Int,
    val followingId: Int
)