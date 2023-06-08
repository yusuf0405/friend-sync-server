package com.joseph.repository

import com.joseph.models.AuthResponse
import com.joseph.models.SignInParams
import com.joseph.models.SignUpParams
import com.joseph.models.User
import com.joseph.util.Response

interface UserRepository {

    suspend fun signUp(params: SignUpParams): Response<AuthResponse>

    suspend fun signIn(params: SignInParams): Response<AuthResponse>
}