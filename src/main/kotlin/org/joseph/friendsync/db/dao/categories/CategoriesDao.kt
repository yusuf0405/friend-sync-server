package org.joseph.friendsync.db.dao.categories

import org.joseph.friendsync.models.categories.Category

interface CategoriesDao {

    suspend fun addNewCategory(categoryName: String): Category?

    suspend fun deleteCategoryById(id: Int)

    suspend fun fetchAllCategories(): List<Category>

    suspend fun fetchCategoryById(id: Int): Category?
}