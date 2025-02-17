package org.softsuave.bustlespot

import co.touchlab.kermit.Logger

object Log {
    private val logger = Logger.withTag("BustleSpotLogger")

    fun d(message: String) = logger.d { message }
    fun i(message: String) = logger.i { message }
    fun w(message: String) = logger.w { message }
    fun e(message: String, throwable: Throwable? = null) = logger.e(throwable) { message }

}