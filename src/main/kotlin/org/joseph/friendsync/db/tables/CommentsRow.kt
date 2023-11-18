package org.joseph.friendsync.db.tables

import org.jetbrains.exposed.sql.Table

object CommentsRow : Table(name = "comments") {
    val id = integer(name = "id").autoIncrement()
    val comment = text(name = "comment")
    val releaseDate = long(name = "release_date")
    val likesCount = integer(name = "likes_count")
    val userId = reference("user_id", UserRow.id)
    val postId = reference("postId", PostRow.id)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}

