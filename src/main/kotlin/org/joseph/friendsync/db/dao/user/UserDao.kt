package org.joseph.friendsync.db.dao.user

import org.joseph.friendsync.models.auth.SignUpParams
import org.joseph.friendsync.models.auth.User
import org.joseph.friendsync.models.user.EditProfileParams
import org.joseph.friendsync.models.user.UserInfo
import org.joseph.friendsync.models.user.UserPersonalInfo
import org.joseph.friendsync.models.user.UserPersonalInfoResponse
import org.joseph.friendsync.util.Response

interface UserDao {

    suspend fun insert(params: SignUpParams): User?

    suspend fun findByEmail(email: String): User?

    suspend fun fetchOnboardingUsers(userId: Int): List<UserInfo>

    suspend fun fetchUserPersonalInfoById(userId: Int): UserPersonalInfo?

    suspend fun searchUsers(page: Int, pageSize: Int, query: String): List<UserInfo>

    suspend fun fetchUserDetailById(userId: Int): User?

    suspend fun editUserParams(params: EditProfileParams): EditProfileParams
}