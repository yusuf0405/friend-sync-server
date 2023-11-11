package org.joseph.friendsync.repository.user

import com.joseph.models.user.UserDetailResponse
import com.joseph.models.user.UserListResponse
import com.joseph.util.Response

interface UserRepository {

    suspend fun fetchOnboardingUsers(userId: Int): Response<UserListResponse>

    suspend fun searchUsers(
        page: Int,
        pageSize: Int,
        query: String
    ): Response<UserListResponse>

    suspend fun fetchUserById(userId: Int): Response<UserDetailResponse>
}