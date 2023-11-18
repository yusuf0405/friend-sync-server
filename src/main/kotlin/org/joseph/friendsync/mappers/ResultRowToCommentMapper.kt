package org.joseph.friendsync.mappers

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.joseph.friendsync.db.tables.CommentsRow
import org.joseph.friendsync.db.tables.UserRow
import org.joseph.friendsync.models.comments.Comment
import org.joseph.friendsync.models.user.UserInfo

class ResultRowToCommentMapper(
    private val resultRowToUserSmallInfoMapper: ResultRowToUserInfoMapper
) : Mapper<ResultRow, Comment> {

    override fun map(from: ResultRow): Comment = from.run {
        val userRow = UserRow.select { UserRow.id eq from[CommentsRow.userId] }.singleOrNull()
        Comment(
            id = this[CommentsRow.id],
            comment = this[CommentsRow.comment],
            likesCount = this[CommentsRow.likesCount],
            postId = this[CommentsRow.postId],
            user = if (userRow != null) resultRowToUserSmallInfoMapper.map(userRow) else UserInfo.unknown,
            releaseDate = this[CommentsRow.releaseDate],
        )
    }

}