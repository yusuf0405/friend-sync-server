package org.joseph.friendsync.mappers

import org.jetbrains.exposed.sql.ResultRow
import org.joseph.friendsync.db.tables.UserRow
import org.joseph.friendsync.models.user.UserInfo

class ResultRowToUserInfoMapper : Mapper<ResultRow, UserInfo> {

    override fun map(from: ResultRow): UserInfo = from.run {
        UserInfo(
            id = get(UserRow.id),
            name = get(UserRow.firstName),
            lastName = get(UserRow.lastName),
            avatar = get(UserRow.avatar),
            releaseDate = get(UserRow.release_date),
        )
    }
}