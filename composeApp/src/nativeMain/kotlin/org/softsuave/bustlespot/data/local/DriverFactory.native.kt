package org.softsuave.bustlespot.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.Database
import kotlinx.serialization.KeepGeneratedSerializer

actual fun createDriver(): SqlDriver {
    return NativeSqliteDriver(Database.Schema, DB_NAME)
}
