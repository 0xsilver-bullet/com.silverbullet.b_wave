package com.silverbullet.core.databse

import com.silverbullet.core.databse.table.ConnectionsTable
import com.silverbullet.core.databse.table.UsersTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val driverClassname = config.property("db.driverClassName").getString()
        val jdbcUrl = config.property("db.jdbcUrl").getString()
        val username = config.property("db.username").getString()
        val password = config.property("db.password").getString()
        val globalConfig = DatasourceConfig(
            driverClassname,
            jdbcUrl,
            username,
            password
        )
        val hikariDataSource = hikari(globalConfig)
        val database = Database.connect(hikariDataSource)
        transaction(database) {
            SchemaUtils.create(
                UsersTable,
                ConnectionsTable
            )
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun hikari(globalConfig: DatasourceConfig): HikariDataSource {
        val config = HikariConfig()
            .apply {
                driverClassName = globalConfig.driverClassname
                jdbcUrl = globalConfig.jdbcUrl
                username = globalConfig.username
                password = globalConfig.password
                maximumPoolSize = 3
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                validate()
            }
        return HikariDataSource(config)
    }

    private data class DatasourceConfig(
        val driverClassname: String,
        val jdbcUrl: String,
        val username: String,
        val password: String
    )
}