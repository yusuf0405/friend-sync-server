package com.joseph.models.subscription

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionCountResponse(
    val data: ResultSubscriptionCount? = null,
    val errorMessage: String? = null
)
@Serializable
data class ResultSubscriptionCount(
    val followingCount: Int,
)