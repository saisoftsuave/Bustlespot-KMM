package org.softsuave.bustlespot.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.Database

actual fun createDriver(): SqlDriver {
    return NativeSqliteDriver(Database.Schema, DB_NAME)
}
