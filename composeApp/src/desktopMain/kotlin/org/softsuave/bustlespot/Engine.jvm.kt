package org.softsuave.bustlespot

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

actual fun getEngine(): HttpClientEngine {
    return CIO.create()
}