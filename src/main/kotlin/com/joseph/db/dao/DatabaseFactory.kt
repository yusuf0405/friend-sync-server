package com.joseph.db.dao

import com.joseph.db.tables.PostImageUrlRow
import com.joseph.db.tables.PostRow
import com.joseph.db.tables.SubscriptionRow
import com.joseph.db.tables.UserRow
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