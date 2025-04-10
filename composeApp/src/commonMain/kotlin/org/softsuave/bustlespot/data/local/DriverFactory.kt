package org.softsuave.bustlespot.data.local

import app.cash.sqldelight.db.SqlDriver
import org.softsuave.bustlespot.Log


const val DB_NAME = "sqldelight.db"


fun migrateDB(driver: SqlDriver){
    //Excute needed changes Quary manually
    driver.execute(
        null,
        """
                CREATE TABLE IF NOT EXISTS ActivityData_new (
                    activityId INTEGER PRIMARY KEY AUTOINCREMENT,
                    taskId INTEGER,
                    projectId INTEGER,
                    startTime TEXT,
                    endTime TEXT,
                    mouseActivity INTEGER DEFAULT 0,
                    keyboardActivity INTEGER DEFAULT 0,
                    totalActivity INTEGER DEFAULT 0,
                    billable TEXT DEFAULT '',
                    notes TEXT,
                    organisationId INTEGER,
                    uri TEXT,
                    unTrackedTime INTEGER,
                    isFailed INTEGER DEFAULT 0
                );
                """.trimIndent(),
        0
    )

    driver.execute(
        null,
        """
                INSERT INTO ActivityData_new (
                    taskId, projectId, startTime, endTime, mouseActivity, keyboardActivity, totalActivity,
                    billable, notes, organisationId, uri, unTrackedTime, isFailed
                )
                SELECT taskId, projectId, startTime, endTime, mouseActivity, keyboardActivity, totalActivity,
                       billable, notes, organisationId, uri, unTrackedTime, isFailed
                FROM ActivityData;
                """.trimIndent(),
        0
    )

    driver.execute(null, "DROP TABLE ActivityData;", 0)
    driver.execute(null, "ALTER TABLE ActivityData_new RENAME TO ActivityData;", 0)
    Log.d("migration done")
}
expect fun createDriver(): SqlDriver
//
//fun createDatabase(driverFactory: DriverFactory): Database {
//  val driver = driverFactory.createDriver()
//  val database = Database(driver)
//
//  // Do more work with the database (see below).
//  return database
//}