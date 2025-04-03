package org.softsuave.bustlespot.data.local


import androidx.annotation.Keep
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.Database
import org.softsuave.bustlespot.screenshot.ComponentActivityReference

@Keep
actual fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(Database.Schema,  context = AppContextWrapper.appContext
        ?: throw IllegalStateException("Application context is not initialized!"), DB_NAME)
}

