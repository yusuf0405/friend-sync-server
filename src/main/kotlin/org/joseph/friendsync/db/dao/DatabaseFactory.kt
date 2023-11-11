package org.joseph.friendsync.db.dao

import org.joseph.friendsync.db.tables.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(createHikariDataSource())
        transaction {
            SchemaUtils.create(UserRow)
            SchemaUtils.create(PostImageUrlRow)
            SchemaUtils.create(PostRow)
            SchemaUtils.create(SubscriptionRow)
            SchemaUtils.create(CategoriesRow)
        }
    }

    private fun createHikariDataSource(): HikariDataSource {
        val driverClass = "org.postgresql.Driver"
        val jdbcUrl = "jdbc:postgresql://localhost:5432/socialmediadb"
        val hikariConfig = HikariConfig().apply {
            driverClassName = driverClass
            setJdbcUrl(jdbcUrl)
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        return HikariDataSource(hikariConfig)
    }


    suspend fun <T> dbQuery(
        block: suspend () -> T
    ) = newSuspendedTransaction(Dispatchers.IO) { block() }
}