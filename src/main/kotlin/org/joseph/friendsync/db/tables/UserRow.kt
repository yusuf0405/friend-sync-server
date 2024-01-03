package org.joseph.friendsync.db.tables

import org.jetbrains.exposed.sql.Table

object UserRow : Table(name = "users") {
    val id = integer(name = "user_id").autoIncrement()
    val firstName = varchar(name = "first_name", length = 250)
    val lastName = varchar(name = "last_name", length = 250)
    val email = varchar(name = "user_email", length = 250)
    val password = varchar(name = "user_password", length = 100)
    val release_date = long(name = "release_date")

    val bio = text(name = "user_bio").nullable()
    val avatar = text(name = "user_avatar").nullable()
    val profileBackground = text(name = "profile_background").nullable()
    val education = text(name = "education").nullable()

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}