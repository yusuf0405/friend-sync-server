package org.joseph.friendsync.mappers

import org.joseph.friendsync.db.tables.CategoriesRow
import org.joseph.friendsync.models.categories.Category
import org.jetbrains.exposed.sql.ResultRow


class ResultRowToCategoryMapper : Mapper<ResultRow, Category> {
    override fun map(from: ResultRow): Category = from.run {
        Category(
            name = this[CategoriesRow.name],
            id = this[CategoriesRow.id]
        )
    }

}