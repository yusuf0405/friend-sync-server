package com.joseph.db.dao.user

import com.joseph.db.dao.DatabaseFactory.dbQuery
import com.joseph.db.dao.subscription.SubscriptionDao
import com.joseph.db.tables.SubscriptionRow
import com.joseph.db.tables.UserRow
import com.joseph.mappers.ResultRowToUserInfoMapper
import com.joseph.mappers.ResultRowToUserMapper
import com.joseph.models.auth.*
import com.joseph.models.user.UserInfo
import com.joseph.security.hashPassword
import org.jetbrains.exposed.sql.*
import java.util.*

class UserDaoImpl(
    private val subscriptionDao: SubscriptionDao,
    private val resultRowToUserMapper: ResultRowToUserMapper,
    private val resultRowToUserInfoMapper: ResultRowToUserInfoMapper,
) : UserDao {

    override suspend fun insert(
        params: SignUpParams
    ): User? {
        return dbQuery {
            val insertStatement = UserRow.insert {
                it[firstName] = params.name
                it[lastName] = params.lastName
                it[email] = params.email
                it[release_date] = System.currentTimeMillis()
                it[password] = hashPassword(params.password)
            }
            val result = insertStatement.resultedValues?.singleOrNull()
            if (result != null) resultRowToUserMapper.map(result)
            else null
        }
    }

    override suspend fun findByEmail(email: String): User? {
        return dbQuery {
            UserRow.select { UserRow.email eq email }
                .map(resultRowToUserMapper::map)
                .singleOrNull()
        }
    }

    /**
     *  Получить пользователей, на которых данный пользователь не подписан,
     *  но на которых подписаны другие пользователи, на которых данный пользователь подписан.
     */
    override suspend fun fetchOnboardingUsers(userId: Int): List<UserInfo> {
        return dbQuery {
            // Шаг 1: Получаем список пользователей, на которых подписан указанный пользователь
            val subscribedUsersIds = subscriptionDao.fetchSubscriptionUserIds(userId)

            // Шаг 2: Получаем список пользователей, на которых подписаны пользователи из шага 1
            val subscribersIds = SubscriptionRow
                .select { SubscriptionRow.followerId inList subscribedUsersIds }
                .map { it[SubscriptionRow.followingId] }

            // Шаг 3: Исключаем из списка из шага 2 тех пользователей, на которых подписан указанный пользователь
            val recommendedUserIds = subscribersIds - subscribedUsersIds.toSet()

            /* Получаем информацию о рекомендованных пользователях из таблицы Users */
            return@dbQuery UserRow.select { UserRow.id inList recommendedUserIds }
                .map(resultRowToUserInfoMapper::map)
        }
    }

    override suspend fun searchUsers(
        page: Int,
        pageSize: Int,
        query: String
    ): List<UserInfo> {
        val offset = (page - 1) * pageSize
        // Добавлена подстановочный знак "%" в запрос для частичного совпадения
        val searchQuery = "%${query.lowercase(Locale.getDefault())}%"

        return dbQuery {
            UserRow.select { (UserRow.firstName.lowerCase() like searchQuery) or (UserRow.lastName.lowerCase() like query) }
                .orderBy(UserRow.release_date, SortOrder.DESC)
                .limit(pageSize, offset.toLong())
                .map(resultRowToUserInfoMapper::map)
        }
    }

    override suspend fun fetchUserDetailById(userId: Int): User? {
        return dbQuery {
            UserRow.select { UserRow.id eq userId }
                .map(resultRowToUserMapper::map)
                .singleOrNull()
        }
    }
}