package com.joseph.dao.user

import com.joseph.dao.DatabaseFactory.dbQuery
import com.joseph.models.SignUpParams
import com.joseph.models.User
import com.joseph.models.UserRow
import com.joseph.security.hashPassword
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserDaoImpl : UserDao {

    override suspend fun insert(
        params: SignUpParams
    ): User? {
        return dbQuery {
            val insertStatement = UserRow.insert {
                it[name] = params.name
                it[email] = params.email
                it[password] = hashPassword(params.password)
            }

            insertStatement.resultedValues?.singleOrNull()?.let(::rowToUser)
        }
    }

    override suspend fun findByEmail(email: String): User? {
        return dbQuery {
            UserRow.select { UserRow.email eq email }
                .map(::rowToUser)
                .singleOrNull()
        }
    }

    private fun rowToUser(row: ResultRow): User {
        return User(
            id = row[UserRow.id],
            name = row[UserRow.name],
            bio = row[UserRow.bio],
            avatar = row[UserRow.avatar],
            password = row[UserRow.password],
        )
    }
}