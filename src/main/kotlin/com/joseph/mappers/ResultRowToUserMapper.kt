package com.joseph.mappers

import com.joseph.db.tables.UserRow
import com.joseph.models.auth.User
import org.jetbrains.exposed.sql.ResultRow

class ResultRowToUserMapper : Mapper<ResultRow, User> {
    override fun map(from: ResultRow): User = from.run {
        User(
            id = this[UserRow.id],
            name = this[UserRow.firstName],
            lastName = this[UserRow.lastName],
            bio = this[UserRow.bio],
            avatar = this[UserRow.avatar],
            password = this[UserRow.password],
            education = this[UserRow.education],
            profileBackground = this[UserRow.profileBackground],
            releaseDate = this[UserRow.release_date],
        )
    }
}