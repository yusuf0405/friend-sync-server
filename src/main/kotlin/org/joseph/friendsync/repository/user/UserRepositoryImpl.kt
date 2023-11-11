package org.joseph.friendsync.repository.user

import org.joseph.friendsync.db.dao.subscription.SubscriptionDao
import org.joseph.friendsync.db.dao.user.UserDao
import org.joseph.friendsync.models.auth.*
import org.joseph.friendsync.models.user.UserDetail
import org.joseph.friendsync.models.user.UserDetailResponse
import org.joseph.friendsync.models.user.UserListResponse
import org.joseph.friendsync.plugins.generateToken
import org.joseph.friendsync.repository.subscription.something_went_wrong
import org.joseph.friendsync.security.hashPassword
import org.joseph.friendsync.util.Response
import io.ktor.http.*

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val subscriptionDao: SubscriptionDao,
) : UserRepository {

    override suspend fun fetchOnboardingUsers(userId: Int): Response<UserListResponse> {
        return try {
            val users = userDao.fetchOnboardingUsers(userId)
            Response.Success(
                data = UserListResponse(
                    data = users
                )
            )
        } catch (e: Throwable) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = UserListResponse(
                    errorMessage = something_went_wrong
                )
            )
        }
    }

    override suspend fun searchUsers(page: Int, pageSize: Int, query: String): Response<UserListResponse> {
        return try {
            val users = userDao.searchUsers(
                page = page,
                pageSize = pageSize,
                query = query
            )
            Response.Success(
                data = UserListResponse(
                    data = users
                )
            )
        } catch (e: Throwable) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = UserListResponse(
                    errorMessage = something_went_wrong
                )
            )
        }
    }

    override suspend fun fetchUserById(userId: Int): Response<UserDetailResponse> {
        return try {
            val user = userDao.fetchUserDetailById(userId)
            if (user == null) Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = UserDetailResponse(errorMessage = "The user with this ID was not found!")
            )
            else Response.Success(
                data = UserDetailResponse(
                    data = UserDetail(
                        id = user.id,
                        bio = user.bio,
                        lastName = user.lastName,
                        name = user.name,
                        avatar = user.avatar,
                        profileBackground = user.profileBackground,
                        education = user.education,
                        releaseDate = user.releaseDate,
                        followersCount = subscriptionDao.fetchSubscriptionCount(userId),
                        followingCount = subscriptionDao.fetchFollowingCount(userId)
                    )
                )
            )
        } catch (e: Throwable) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = UserDetailResponse(
                    errorMessage = something_went_wrong
                )
            )
        }
    }
}