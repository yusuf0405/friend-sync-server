package org.joseph.friendsync.db.tables

import org.jetbrains.exposed.sql.Table

object CategoriesRow : Table(name = "categories") {
    val id = integer(name = "id").autoIncrement()
    val name = text(name = "image_url")

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}