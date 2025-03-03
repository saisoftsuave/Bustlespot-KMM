package org.softsuave.bustlespot.data.local

import app.cash.sqldelight.db.SqlDriver
import com.example.Database

val DB_NAME = "sqldelight.db"


expect fun createDriver(): SqlDriver
//
//fun createDatabase(driverFactory: DriverFactory): Database {
//  val driver = driverFactory.createDriver()
//  val database = Database(driver)
//
//  // Do more work with the database (see below).
//  return database
//}