package org.joseph.friendsync.repository.user

import com.joseph.db.dao.subscription.SubscriptionDao
import com.joseph.db.dao.user.UserDao
import com.joseph.models.auth.*
import com.joseph.models.user.UserDetail
import com.joseph.models.user.UserDetailResponse
import com.joseph.models.user.UserListResponse
import com.joseph.plugins.generateToken
import com.joseph.repository.subscription.something_went_wrong
import com.joseph.security.hashPassword
import com.joseph.util.Response
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