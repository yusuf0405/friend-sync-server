package org.joseph.friendsync.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val name: String
)

@Serializable
data class CategoryResponse(
    val data: Category? = null,
    val errorMessage: String? = null
)

@Serializable
data class CategoriesResponse(
    val data: List<Category>? = null,
    val errorMessage: String? = null
)