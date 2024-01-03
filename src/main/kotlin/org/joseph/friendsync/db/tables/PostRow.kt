package org.joseph.friendsync.db.tables

import org.jetbrains.exposed.sql.Table
object PostRow : Table(name = "new_posts") {
    val id = integer(name = "id").autoIncrement()
    val message = varchar(name = "message", length = 250).nullable()
    val releaseDate = long(name = "release_date")
    val likesCount = integer(name = "likes_count")
    val commentsCount = integer(name = "comments_count")
    val savedCount = integer(name = "saved_count")
    val userId = reference("user_id", UserRow.id)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}

object PostImageUrlRow : Table(name = "image_urls") {
    private val id = integer(name = "id").autoIncrement()
    val postId = reference("post_id", PostRow.id)
    val imageUrl = text(name = "image_url")

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}
