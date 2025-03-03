package org.softsuave.bustlespot.data.local


import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.Database

actual fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(Database.Schema, AppContextWrapper.appContext!!, DB_NAME)
}

