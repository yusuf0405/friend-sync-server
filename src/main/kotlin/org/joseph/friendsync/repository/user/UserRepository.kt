package org.joseph.friendsync.repository.user

import org.joseph.friendsync.models.user.*
import org.joseph.friendsync.util.Response

interface UserRepository {

    suspend fun fetchOnboardingUsers(userId: Int): Response<UserListResponse>

    suspend fun searchUsers(
        page: Int,
        pageSize: Int,
        query: String
    ): Response<UserListResponse>

    suspend fun fetchUserById(userId: Int): Response<UserDetailResponse>

    suspend fun fetchUserPersonalInfoById(userId: Int): Response<UserPersonalInfoResponse>

    suspend fun editUserWithParams(params: EditProfileParams): Response<EditProfileParamsResponse>
}