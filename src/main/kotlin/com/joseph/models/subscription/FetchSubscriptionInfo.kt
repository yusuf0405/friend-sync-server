package com.joseph.models.subscription

import kotlinx.serialization.Serializable

@Serializable
data class FetchSubscriptionInfo(
    val userId: Int,
)