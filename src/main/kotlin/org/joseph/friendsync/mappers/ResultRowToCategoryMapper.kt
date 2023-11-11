package org.joseph.friendsync.mappers

import com.joseph.db.tables.CategoriesRow
import com.joseph.models.categories.Category
import org.jetbrains.exposed.sql.ResultRow


class ResultRowToCategoryMapper : Mapper<ResultRow, Category> {
    override fun map(from: ResultRow): Category = from.run {
        Category(
            name = this[CategoriesRow.name],
            id = this[CategoriesRow.id]
        )
    }

}