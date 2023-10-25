package com.joseph.models.subscription

import kotlinx.serialization.Serializable
@Serializable
data class SubscriptionUserIds(
    val userIds: List<Int>,
)

@Serializable
data class SubscriptionIdsResponse(
    val data: SubscriptionUserIds? = null,
    val errorMessage: String? = null
)