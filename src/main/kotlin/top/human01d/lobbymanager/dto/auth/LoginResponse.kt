package top.human01d.lobbymanager.dto.auth

import java.util.UUID

@Suppress("UNUSED")
class LoginResponse(
    val userId: UUID,
    val token: String,
    val aesKey: String,
)
