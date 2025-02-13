package org.softsuave.bustlespot

class AOSPlatform: Platform {
    override val name: String = "Android"
}
actual fun getPlatform(): Platform = AOSPlatform()