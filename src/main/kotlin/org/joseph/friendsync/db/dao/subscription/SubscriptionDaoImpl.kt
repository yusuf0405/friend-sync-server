package org.joseph.friendsync.db.dao.subscription

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.joseph.friendsync.db.dao.DatabaseFactory.dbQuery
import org.joseph.friendsync.db.tables.SubscriptionRow
import org.joseph.friendsync.db.tables.UserRow
import org.joseph.friendsync.models.subscription.CreateOrCancelSubscription
import org.joseph.friendsync.models.subscription.Subscription
import org.joseph.friendsync.models.subscription.SubscriptionResultUser

class SubscriptionDaoImpl : SubscriptionDao {

    override suspend fun fetchSubscriptionCount(userId: Int): Int {
        return dbQuery {
            SubscriptionRow.select { SubscriptionRow.followingId eq userId }.count().toInt()
        }
    }

    override suspend fun fetchFollowingCount(userId: Int): Int {
        return dbQuery {
            SubscriptionRow.select { SubscriptionRow.followerId eq userId }.count().toInt()
        }
    }

    override suspend fun hasUserSubscription(
        userId: Int,
        followingUserId: Int
    ): Boolean {
        return dbQuery {
            val result = SubscriptionRow.select {
                (SubscriptionRow.followerId eq userId) and (SubscriptionRow.followingId eq followingUserId)
            }
            result.singleOrNull() != null
        }
    }

    override suspend fun fetchSubscriptionUserIds(userId: Int): List<Int> {
        return dbQuery {
            SubscriptionRow.select { SubscriptionRow.followerId eq userId }
                .map { it[SubscriptionRow.followingId] }
        }
    }

    override suspend fun fetchUserSubscriptions(userId: Int): List<Subscription> {
        return dbQuery {
            SubscriptionRow.select { SubscriptionRow.followerId eq userId }
                .map {
                    Subscription(
                        id = it[SubscriptionRow.id],
                        followerId = it[SubscriptionRow.followerId],
                        followingId = it[SubscriptionRow.followingId]
                    )
                }
        }
    }

    override suspend fun fetchSubscriptionUsers(userId: Int): List<SubscriptionResultUser> {
        return dbQuery {
            val followingIds = SubscriptionRow
                .select { SubscriptionRow.followerId eq userId }
                .map { it[SubscriptionRow.followingId] }

            return@dbQuery followingIds.mapNotNull { followingId ->
                val row = UserRow.select { UserRow.id eq followingId }.singleOrNull()
                if (row != null) rowToUser(row)
                else null
            }
        }
    }

    override suspend fun createSubscription(createSubscription: CreateOrCancelSubscription): Int {
        return dbQuery {
            // Check if the subscription already exists
            val existingSubscription = SubscriptionRow.select {
                (SubscriptionRow.followerId eq createSubscription.followerId) and
                        (SubscriptionRow.followingId eq createSubscription.followingId)
            }.count()

            if (existingSubscription == 0L) {
                // Create a new subscription and return its ID
                SubscriptionRow.insert {
                    it[followerId] = createSubscription.followerId
                    it[followingId] = createSubscription.followingId
                }.resultedValues?.singleOrNull()?.get(SubscriptionRow.id) ?: -1
            } else {
                // Return some indicator that the subscription already exists (e.g., -1)
                -1
            }
        }
    }

    override suspend fun cancelSubscription(cancelSubscription: CreateOrCancelSubscription): Int {
        return dbQuery {
            // Get the ID of the subscription to be canceled
            val subscriptionId = SubscriptionRow
                .slice(SubscriptionRow.id)
                .select {
                    (SubscriptionRow.followerId eq cancelSubscription.followerId) and
                            (SubscriptionRow.followingId eq cancelSubscription.followingId)
                }
                .singleOrNull()?.get(SubscriptionRow.id)

            if (subscriptionId != null) {
                // Delete the subscription and return its ID
                SubscriptionRow.deleteWhere {
                    (followerId eq cancelSubscription.followerId) and
                            (followingId eq cancelSubscription.followingId)
                }
                subscriptionId ?: -1
            } else {
                // Return some indicator that the subscription does not exist (e.g., -1)
                -1
            }
        }
    }

    private fun rowToUser(row: ResultRow): SubscriptionResultUser {
        return SubscriptionResultUser(
            id = row[UserRow.id],
            name = row[UserRow.lastName],
            bio = row[UserRow.bio],
            avatar = row[UserRow.avatar],
        )
    }
}