package org.joseph.friendsync.mappers

import org.jetbrains.exposed.sql.ResultRow
import org.joseph.friendsync.db.tables.UserRow
import org.joseph.friendsync.models.user.UserPersonalInfo

class ResultRowToUserPersonalInfoMapper : Mapper<ResultRow, UserPersonalInfo> {

    override fun map(from: ResultRow): UserPersonalInfo = from.run {
        UserPersonalInfo(
            id = this[UserRow.id],
            email = this[UserRow.email],
        )
    }
}