package org.joseph.friendsync.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class AddCategoryParams(
    val category_name: String,
)