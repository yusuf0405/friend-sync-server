package com.joseph.models.subscription

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionUserResponse(
    val data: List<SubscriptionResultUser>? = null,
    val errorMessage: String? = null
)

@Serializable
data class SubscriptionResultUser(
    val id: Int,
    val name: String,
    val bio: String?,
    val avatar: String?,
)