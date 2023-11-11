package org.joseph.friendsync.db.dao.subscription

import com.joseph.db.dao.DatabaseFactory.dbQuery
import com.joseph.db.tables.SubscriptionRow
import com.joseph.db.tables.UserRow
import com.joseph.models.subscription.CreateOrCancelSubscription
import com.joseph.models.subscription.SubscriptionResultUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class SubscriptionDaoImpl : SubscriptionDao {
    override suspend fun fetchSubscriptionCount(userId: Int): Int {
        return dbQuery {
            SubscriptionRow.select { SubscriptionRow.followerId eq userId }.count().toInt()
        }
    }

    override suspend fun fetchFollowingCount(userId: Int): Int {
        return dbQuery {
            SubscriptionRow.select { SubscriptionRow.followingId eq userId }.count().toInt()
        }
    }

    override suspend fun fetchSubscriptionUserIds(userId: Int): List<Int> {
        return dbQuery {
            SubscriptionRow.select { SubscriptionRow.followerId eq userId }
                .map { it[SubscriptionRow.followingId] }
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

    override suspend fun createSubscription(
        createSubscription: CreateOrCancelSubscription
    ): Int {
        dbQuery {
            // Проверяем, не существует ли уже такой подписки
            val existingSubscription = SubscriptionRow.select {
                (SubscriptionRow.followerId eq createSubscription.followerId) and
                        (SubscriptionRow.followingId eq createSubscription.followingId)
            }.count()

            if (existingSubscription == 0L) {
                // Создаем новую подписку
                SubscriptionRow.insert {
                    it[followerId] = createSubscription.followerId
                    it[followingId] = createSubscription.followingId
                }
            }
        }
        return fetchSubscriptionCount(createSubscription.followerId)
    }

    override suspend fun cancelSubscription(cancelSubscription: CreateOrCancelSubscription): Int {
        dbQuery {
            SubscriptionRow.deleteWhere {
                (followerId eq cancelSubscription.followerId) and
                        (followingId eq cancelSubscription.followingId)
            }
        }
        return fetchSubscriptionCount(cancelSubscription.followerId)
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