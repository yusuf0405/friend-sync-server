package com.joseph.repository.user

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

    override suspend fun signUp(params: SignUpParams): Response<AuthResponse> {
        return if (userAlreadyExist(params.email)) {
            Response.Error(
                code = HttpStatusCode.Conflict,
                data = AuthResponse(
                    errorMessage = "A user with this email already exists!",
                )
            )
        } else {
            val insertedUser = userDao.insert(params)
            if (insertedUser == null) {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = AuthResponse(
                        errorMessage = "Ooops, sorry we could not register the user, try later!",
                    )
                )
            } else {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            id = insertedUser.id,
                            name = insertedUser.name,
                            lastName = insertedUser.lastName,
                            bio = insertedUser.bio,
                            token = generateToken(params.email),
                        )
                    )
                )
            }
        }
    }

    override suspend fun signIn(params: SignInParams): Response<AuthResponse> {
        val user = userDao.findByEmail(params.email)
        return if (user == null) {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = AuthResponse(
                    errorMessage = "Invalid credentials, no user with thies email!"
                )
            )
        } else {
            val hashPassword = hashPassword(params.password)
            if (user.password == hashPassword) {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            id = user.id,
                            name = user.name,
                            lastName = user.lastName,
                            avatar = user.avatar,
                            bio = user.bio,
                            token = generateToken(params.email),
                        )
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Forbidden,
                    data = AuthResponse(
                        errorMessage = "Invalid credentials, wrong password!"
                    )
                )
            }
        }
    }

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

    private suspend fun userAlreadyExist(email: String): Boolean {
        return userDao.findByEmail(email) != null
    }
}