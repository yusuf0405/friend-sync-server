package org.joseph.friendsync.db.dao.user

import org.joseph.friendsync.db.dao.DatabaseFactory.dbQuery
import org.joseph.friendsync.db.dao.subscription.SubscriptionDao
import org.joseph.friendsync.db.tables.SubscriptionRow
import org.joseph.friendsync.db.tables.UserRow
import org.joseph.friendsync.mappers.ResultRowToUserInfoMapper
import org.joseph.friendsync.mappers.ResultRowToUserMapper
import org.joseph.friendsync.models.auth.*
import org.joseph.friendsync.models.user.UserInfo
import org.joseph.friendsync.security.hashPassword
import org.jetbrains.exposed.sql.*
import org.joseph.friendsync.mappers.ResultRowToUserPersonalInfoMapper
import org.joseph.friendsync.models.user.EditProfileParams
import org.joseph.friendsync.models.user.UserPersonalInfo
import java.util.*

class UserDaoImpl(
    private val subscriptionDao: SubscriptionDao,
    private val resultRowToUserMapper: ResultRowToUserMapper,
    private val resultRowToUserPersonalInfoMapper: ResultRowToUserPersonalInfoMapper,
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
     * Получает список новых пользователей для указанного пользователя.
     *
     * Этот метод возвращает список рекомендованных для введенного пользователя на основе его подписок.
     * Процесс включает в себя выявление пользователей, на которых подписан указанный пользователь,
     * поиск пользователей, на которых подписаны эти подписчики, и фильтрацию пользователей, на которых
     * уже подписан указанный пользователь. В результате получается список рекомендованных пользователей,
     * включая тех из подписок и дополнительных пользователей для достижения количества 10.
     *
     * @param userId Уникальный идентификатор пользователя, для которого получаются новые пользователи.
     * @return Список объектов [UserInfo], представляющих новых пользователей.
     *
     * @throws [Throwable], если происходит ошибка во время выполнения запроса к базе данных.
     */
    override suspend fun fetchOnboardingUsers(userId: Int): List<UserInfo> {
        return dbQuery {
            // Шаг 1: Получаем список пользователей, на которых подписан указанный пользователь
            val subscribedUsersIds = subscriptionDao.fetchSubscriptionUserIds(userId)

            // Проверяем, подписан ли пользователь на максимально допустимое количество (11 пользователей)
            if (subscribedUsersIds.size == 11) return@dbQuery emptyList()

            // Шаг 2: Получаем список пользователей, на которых подписаны пользователи из шага 1
            val subscribersIds = SubscriptionRow
                .select { SubscriptionRow.followerId inList subscribedUsersIds }
                .map { it[SubscriptionRow.followingId] }

            // Шаг 3: Исключаем из списка из шага 2 тех пользователей, на которых подписан указанный пользователь
            val recommendedUserIds = subscribersIds - subscribedUsersIds.toSet()

            /* Получаем информацию о рекомендованных пользователях из таблицы Users */
            val onboardingUsers = UserRow.select { UserRow.id inList recommendedUserIds }
                .map { resultRowToUserInfoMapper.map(it) }

            if (onboardingUsers.size < 10) {
                // Если рекомендованных пользователей меньше 10, дополним результат пользователями,
                // на которых пользователь еще не подписан
                val remainingUsers = fetchUsersNotSubscribed(userId, 10 - onboardingUsers.size)
                onboardingUsers + remainingUsers.filter { it.id != userId }
            } else {
                // Если рекомендованных пользователей больше или равно 10, возвращаем первые 10
                onboardingUsers.take(10)
            }.toSet().toList()
        }
    }

    override suspend fun fetchUserPersonalInfoById(userId: Int): UserPersonalInfo? {
        return dbQuery {
            UserRow.select { UserRow.id eq userId }
                .map(resultRowToUserPersonalInfoMapper::map)
                .singleOrNull()
        }
    }

    // Функция для получения пользователей, на которых пользователь еще не подписан
    private suspend fun fetchUsersNotSubscribed(userId: Int, count: Int): List<UserInfo> {
        val subscribedUsersIds = subscriptionDao.fetchSubscriptionUserIds(userId)
        return dbQuery {
            UserRow.select { UserRow.id notInList subscribedUsersIds }
                .limit(count)
                .map { resultRowToUserInfoMapper.map(it) }
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
                .map { resultRowToUserInfoMapper.map(it) }
        }
    }

    override suspend fun fetchUserDetailById(userId: Int): User? {
        return dbQuery {
            UserRow.select { UserRow.id eq userId }
                .map(resultRowToUserMapper::map)
                .singleOrNull()
        }
    }

    override suspend fun editUserParams(params: EditProfileParams): EditProfileParams {
        return dbQuery {

            val user = UserRow.select { UserRow.id eq params.id }
                .map(resultRowToUserMapper::map)
                .singleOrNull() ?: throw IllegalArgumentException("User not found")

            if (user.email != params.email && findByEmail(params.email) != null) {
                throw IllegalArgumentException("A user with this email already exists!")
            }

            // Выполняем обновление пользователя в базе данных
            UserRow.update({ UserRow.id eq params.id }) {
                it[firstName] = params.name
                it[lastName] = params.lastName
                it[email] = params.email
                it[bio] = params.bio
                it[education] = params.education
                it[avatar] = params.avatar
            }

            return@dbQuery params
        }
    }
}