package org.joseph.friendsync.models.likes

import kotlinx.serialization.Serializable

@Serializable
data class LikedListIdsResponse(
    val data: LikedListIds? = null,
    val errorMessage: String? = null
)

@Serializable
data class LikedListIds(
    val ids: List<Int>,
)