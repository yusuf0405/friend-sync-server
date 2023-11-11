package org.joseph.friendsync.db.dao.categories

import com.joseph.db.dao.DatabaseFactory.dbQuery
import com.joseph.db.tables.CategoriesRow
import com.joseph.mappers.ResultRowToCategoryMapper
import com.joseph.models.categories.Category
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class CategoriesDaoImpl(
    private val resultRowToCategoryMapper: ResultRowToCategoryMapper
) : CategoriesDao {

    override suspend fun addNewCategory(categoryName: String): Category? {
        return dbQuery {
            val result = CategoriesRow.insert {
                it[name] = categoryName
            }.resultedValues?.singleOrNull()
            if (result == null) null else resultRowToCategoryMapper.map(result)
        }
    }

    override suspend fun deleteCategoryById(id: Int) {
        return dbQuery {
            CategoriesRow.deleteWhere { this.id eq id }
        }
    }

    override suspend fun fetchAllCategories(): List<Category> {
        return dbQuery {
            CategoriesRow.selectAll().map(resultRowToCategoryMapper::map)
        }
    }

    override suspend fun fetchCategoryById(id: Int): Category? {
        return dbQuery {
            val result = CategoriesRow.select {
                CategoriesRow.id eq id
            }.singleOrNull()
            if (result == null) null else resultRowToCategoryMapper.map(result)
        }
    }
}