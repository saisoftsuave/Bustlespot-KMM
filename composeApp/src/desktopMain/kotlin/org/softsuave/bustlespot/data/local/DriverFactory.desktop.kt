package org.softsuave.bustlespot.data.local

import androidx.annotation.Keep
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

import com.example.Database
import java.io.File

@Keep
actual fun createDriver(): SqlDriver {
    val databaseDir = File(System.getProperty("user.home"), ".myapp")
    databaseDir.mkdirs()
    val databaseFile = File(databaseDir, "database.db")
    val databaseUrl = "jdbc:sqlite:${databaseFile.absolutePath}"
    val driver: SqlDriver = JdbcSqliteDriver(databaseUrl)
    Database.Schema.create(driver)
    return driver
}
