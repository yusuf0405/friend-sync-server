package org.joseph.friendsync.repository.categories

import org.joseph.friendsync.models.categories.CategoriesResponse
import org.joseph.friendsync.models.categories.CategoryResponse
import org.joseph.friendsync.util.Response

interface CategoriesRepository {

    suspend fun addNewCategory(categoryName: String): Response<CategoryResponse>

    suspend fun deleteCategoryById(id: Int): Response<String>

    suspend fun fetchAllCategories(): Response<CategoriesResponse>

    suspend fun fetchCategoryById(id: Int): Response<CategoryResponse>
}