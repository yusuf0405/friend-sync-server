package org.joseph.friendsync.repository.user

import io.ktor.http.*
import org.joseph.friendsync.db.dao.subscription.SubscriptionDao
import org.joseph.friendsync.db.dao.user.UserDao
import org.joseph.friendsync.models.auth.AuthResponse
import org.joseph.friendsync.models.user.*
import org.joseph.friendsync.repository.subscription.something_went_wrong
import org.joseph.friendsync.util.Response

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
                        followingCount = subscriptionDao.fetchFollowingCount(userId),
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

    override suspend fun fetchUserPersonalInfoById(userId: Int): Response<UserPersonalInfoResponse> {
        return try {
            val user = userDao.fetchUserPersonalInfoById(userId)
            if (user == null) Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = UserPersonalInfoResponse(errorMessage = "The user with this ID was not found!")
            )
            else Response.Success(
                data = UserPersonalInfoResponse(
                    data = user
                )
            )
        } catch (e: Throwable) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = UserPersonalInfoResponse(
                    errorMessage = something_went_wrong
                )
            )
        }
    }

    override suspend fun editUserWithParams(params: EditProfileParams): Response<EditProfileParamsResponse> {
        return try {
            val result = userDao.editUserParams(params)
            Response.Success(
                data = EditProfileParamsResponse(
                    data = result
                )
            )
        } catch (e: Throwable) {
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = EditProfileParamsResponse(
                    errorMessage = something_went_wrong
                )
            )
        }
    }
}