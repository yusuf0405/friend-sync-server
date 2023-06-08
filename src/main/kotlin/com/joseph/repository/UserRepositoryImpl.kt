package com.joseph.repository

import com.joseph.dao.user.UserDao
import com.joseph.models.AuthResponse
import com.joseph.models.AuthResponseData
import com.joseph.models.SignInParams
import com.joseph.models.SignUpParams
import com.joseph.plugins.generateToken
import com.joseph.security.hashPassword
import com.joseph.util.Response
import io.ktor.http.*

class UserRepositoryImpl(
    private val userDao: UserDao
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