package org.joseph.friendsync.repository.categories

import com.joseph.db.dao.categories.CategoriesDao
import com.joseph.models.categories.CategoriesResponse
import com.joseph.models.categories.CategoryResponse
import com.joseph.repository.subscription.something_went_wrong
import com.joseph.util.Response
import com.joseph.util.extensions.callSafe
import io.ktor.http.*

class CategoriesRepositoryImpl(
    private val categoriesDao: CategoriesDao
) : CategoriesRepository {

    override suspend fun addNewCategory(
        categoryName: String
    ): Response<CategoryResponse> = callSafe(
        defaultValue = defaultCategoryError
    ) {
        val category = categoriesDao.addNewCategory(categoryName) ?: return@callSafe defaultCategoryError
        Response.Success(
            data = CategoryResponse(data = category)
        )
    }


    override suspend fun deleteCategoryById(
        id: Int
    ): Response<String> = callSafe(defaultError) {
        categoriesDao.deleteCategoryById(id)
        Response.Success(data = String())
    }

    override suspend fun fetchAllCategories(
    ): Response<CategoriesResponse> = callSafe(defaultCategoriesError) {
        val categories = categoriesDao.fetchAllCategories()
        Response.Success(
            data = CategoriesResponse(data = categories)
        )
    }

    override suspend fun fetchCategoryById(
        id: Int
    ): Response<CategoryResponse> = callSafe(defaultCategoryError) {
        val category = categoriesDao.fetchCategoryById(id) ?: return@callSafe defaultCategoryError
        Response.Success(
            data = CategoryResponse(data = category)
        )
    }

    private val defaultError = Response.Error(
        code = HttpStatusCode.InternalServerError,
        data = something_went_wrong
    )

    private val defaultCategoryError = Response.Error(
        code = HttpStatusCode.InternalServerError,
        data = CategoryResponse(
            data = null,
            errorMessage = something_went_wrong
        )
    )

    private val defaultCategoriesError = Response.Error(
        code = HttpStatusCode.InternalServerError,
        data = CategoriesResponse(
            data = null,
            errorMessage = something_went_wrong
        )
    )
}