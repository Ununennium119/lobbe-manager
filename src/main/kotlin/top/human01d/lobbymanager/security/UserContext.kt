package top.human01d.lobbymanager.security

import java.util.*

object UserContext {
    private val userContext = ThreadLocal<UUID>()

    fun set(userId: UUID) {
        userContext.set(userId)
    }

    fun get(): UUID? {
        return userContext.get()
    }
}
