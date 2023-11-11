package org.joseph.friendsync.route

import org.joseph.friendsync.models.auth.SignUpParams
import org.joseph.friendsync.models.categories.AddCategoryParams
import org.joseph.friendsync.repository.categories.CategoriesRepository
import org.joseph.friendsync.util.*
import org.joseph.friendsync.util.extensions.invalidCredentialsError
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private const val CATEGORIES_REQUEST_PATH = "/categories"

fun Routing.categoryRoute() {

    val repository by inject<CategoriesRepository>()

    route(path = CATEGORIES_REQUEST_PATH) {
        categoriesList(repository)
        categoryById(repository)
        addCategory(repository)
        deleteCategory(repository)
    }
}

private fun Route.categoriesList(repository: CategoriesRepository) {
    get(LIST_REQUEST_PATCH) {
        val result = repository.fetchAllCategories()
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.categoryById(repository: CategoriesRepository) {
    get("/{$CATEGORY_ID_PARAM}") {
        val categoryId = call.parameters[CATEGORY_ID_PARAM]?.toIntOrNull()
        if (categoryId == null) {
            call.invalidCredentialsError(USER_ID_PARAM)
            return@get
        }

        val result = repository.fetchCategoryById(
            id = categoryId
        )
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.addCategory(repository: CategoriesRepository) {
    post(ADD_REQUEST_PATCH) {
        val params = call.receiveNullable<AddCategoryParams>()
        val categoryName = params?.category_name
        if (categoryName.isNullOrBlank()) {
            call.invalidCredentialsError(CATEGORY_NAME_PARAM)
            return@post
        }

        val result = repository.addNewCategory(categoryName = categoryName)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}

private fun Route.deleteCategory(repository: CategoriesRepository) {
    post(DELETE_REQUEST_PATCH) {
        val categoryId = call.parameters[CATEGORY_ID_PARAM]?.toIntOrNull()
        if (categoryId == null) {
            call.invalidCredentialsError(USER_ID_PARAM)
            return@post
        }

        val result = repository.deleteCategoryById(id = categoryId)
        call.respond(
            status = result.code,
            message = result.data
        )
    }
}