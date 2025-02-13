package org.softsuave.bustlespot
interface Platform {
    val name: String
}

expect fun getPlatform(): Platform