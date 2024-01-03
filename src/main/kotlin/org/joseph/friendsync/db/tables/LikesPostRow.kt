package org.joseph.friendsync.db.tables

import org.jetbrains.exposed.sql.Table

object LikesPostRow : Table(name = "likes_post") {

    val id = integer(name = "id").autoIncrement()
    val userId = reference("user_id", UserRow.id)
    val postId = reference("post_id", PostRow.id)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}

