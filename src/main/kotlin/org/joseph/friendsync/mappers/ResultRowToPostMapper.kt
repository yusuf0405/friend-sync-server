package org.joseph.friendsync.mappers

import com.joseph.db.tables.PostRow
import com.joseph.db.tables.UserRow
import com.joseph.models.post.Post
import com.joseph.models.post.PostUser
import org.jetbrains.exposed.sql.ResultRow

interface ResultRowToPostMapper {

    fun map(row: ResultRow, userRow: ResultRow?, imageUrl: List<String>): Post
}

class ResultRowToPostMapperImpl : ResultRowToPostMapper {
    override fun map(row: ResultRow, userRow: ResultRow?, imageUrl: List<String>) = row.run {
        Post(
            id = this[PostRow.id],
            message = this[PostRow.message],
            imageUrls = imageUrl,
            user = PostUser(
                id = userRow?.get(UserRow.id),
                name = userRow?.get(UserRow.firstName),
                lastName = userRow?.get(UserRow.lastName),
                userImage = userRow?.get(UserRow.avatar),
            ),
            likesCount = this[PostRow.likesCount],
            savedCount = this[PostRow.savedCount],
            commentsCount = this[PostRow.commentsCount],
            releaseDate = this[PostRow.releaseDate],
        )
    }
}