package org.joseph.friendsync.db.dao.user

import org.joseph.friendsync.models.auth.SignUpParams
import org.joseph.friendsync.models.auth.User
import org.joseph.friendsync.models.user.UserInfo

interface UserDao {

    suspend fun insert(params: SignUpParams): User?

    suspend fun findByEmail(email: String): User?

    suspend fun fetchOnboardingUsers(userId: Int): List<UserInfo>

    suspend fun searchUsers(page: Int, pageSize: Int, query: String): List<UserInfo>

    suspend fun fetchUserDetailById(userId: Int): User?
}