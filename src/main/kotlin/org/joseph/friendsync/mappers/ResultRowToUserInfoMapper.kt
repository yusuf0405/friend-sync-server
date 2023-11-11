package org.joseph.friendsync.mappers

import com.joseph.db.tables.UserRow
import com.joseph.models.user.UserInfo
import org.jetbrains.exposed.sql.ResultRow

class ResultRowToUserInfoMapper : Mapper<ResultRow, UserInfo> {
    override fun map(from: ResultRow): UserInfo = from.run {
        UserInfo(
            id = this[UserRow.id],
            name = this[UserRow.firstName],
            lastName = this[UserRow.lastName],
            avatar = this[UserRow.avatar],
            releaseDate = this[UserRow.release_date],
        )
    }
}