package org.joseph.friendsync.mappers

import org.jetbrains.exposed.sql.ResultRow
import org.joseph.friendsync.db.dao.comments.CommentsDao
import org.joseph.friendsync.db.dao.likes.LikesPostDao
import org.joseph.friendsync.db.tables.PostRow
import org.joseph.friendsync.models.post.Post
import org.joseph.friendsync.models.user.UserInfo

interface ResultRowToPostMapper {

    suspend fun map(row: ResultRow, userRow: ResultRow?, imageUrl: List<String>): Post
}

class ResultRowToPostMapperImpl(
    private val resultRowToUserSmallInfoMapper: ResultRowToUserInfoMapper,
    private val commentsDao: CommentsDao,
    private val likesPostDao: LikesPostDao,
) : ResultRowToPostMapper {

    override suspend fun map(row: ResultRow, userRow: ResultRow?, imageUrl: List<String>) = row.run {
        Post(
            id = this[PostRow.id],
            message = this[PostRow.message],
            imageUrls = imageUrl,
            user = if (userRow != null) resultRowToUserSmallInfoMapper.map(userRow) else UserInfo.unknown,
            likesCount = likesPostDao.fetchPostLikedCount(this[PostRow.id]),
            savedCount = this[PostRow.savedCount],
            commentsCount = commentsDao.fetchPostCommentsSize(this[PostRow.id]),
            releaseDate = this[PostRow.releaseDate],
        )
    }
}