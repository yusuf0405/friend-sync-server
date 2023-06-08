package com.joseph.dao.user

import com.joseph.models.SignUpParams
import com.joseph.models.User

interface UserDao {

    suspend fun insert(params: SignUpParams): User?

    suspend fun findByEmail(email: String): User?
}