package com.joseph.repository.user

import com.joseph.models.auth.*
import com.joseph.models.user.UserDetailResponse
import com.joseph.models.user.UserListResponse
import com.joseph.util.Response

interface UserRepository {

    suspend fun signUp(params: SignUpParams): Response<AuthResponse>

    suspend fun signIn(params: SignInParams): Response<AuthResponse>

    suspend fun fetchOnboardingUsers(userId: Int): Response<UserListResponse>

    suspend fun fetchUserById(userId: Int): Response<UserDetailResponse>
}