package org.joseph.friendsync.db.tables

import org.jetbrains.exposed.sql.Table

object SubscriptionRow : Table(name = "subscriptions") {
    private val id = integer(name = "id").autoIncrement()
    val followerId = reference("follower_id", UserRow.id)
    val followingId = reference(name = "following_Id", UserRow.id)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}