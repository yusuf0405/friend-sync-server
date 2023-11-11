package org.joseph.friendsync.repository.auth

import com.joseph.models.auth.AuthResponse
import com.joseph.models.auth.SignInParams
import com.joseph.models.auth.SignUpParams
import com.joseph.util.Response

interface AuthRepository {

    suspend fun signUp(params: SignUpParams): Response<AuthResponse>

    suspend fun signIn(params: SignInParams): Response<AuthResponse>
}