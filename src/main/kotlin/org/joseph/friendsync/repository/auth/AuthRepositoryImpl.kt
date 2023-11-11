package org.joseph.friendsync.repository.auth

import org.joseph.friendsync.db.dao.user.UserDao
import org.joseph.friendsync.models.auth.AuthResponse
import org.joseph.friendsync.models.auth.AuthResponseData
import org.joseph.friendsync.models.auth.SignInParams
import org.joseph.friendsync.models.auth.SignUpParams
import org.joseph.friendsync.plugins.generateToken
import org.joseph.friendsync.security.hashPassword
import org.joseph.friendsync.util.Response
import io.ktor.http.*

class AuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {

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

    private suspend fun userAlreadyExist(email: String): Boolean {
        return userDao.findByEmail(email) != null
    }
}