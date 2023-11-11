package org.joseph.friendsync.repository.auth

import org.joseph.friendsync.models.auth.AuthResponse
import org.joseph.friendsync.models.auth.SignInParams
import org.joseph.friendsync.models.auth.SignUpParams
import org.joseph.friendsync.util.Response

interface AuthRepository {

    suspend fun signUp(params: SignUpParams): Response<AuthResponse>

    suspend fun signIn(params: SignInParams): Response<AuthResponse>
}