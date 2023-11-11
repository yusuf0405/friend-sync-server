package org.joseph.friendsync.repository.categories

import com.joseph.models.categories.CategoriesResponse
import com.joseph.models.categories.CategoryResponse
import com.joseph.util.Response

interface CategoriesRepository {

    suspend fun addNewCategory(categoryName: String): Response<CategoryResponse>

    suspend fun deleteCategoryById(id: Int): Response<String>

    suspend fun fetchAllCategories(): Response<CategoriesResponse>

    suspend fun fetchCategoryById(id: Int): Response<CategoryResponse>
}