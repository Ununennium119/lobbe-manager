package top.human01d.lobbymanager.service

import org.springframework.stereotype.Service
import top.human01d.lobbymanager.dto.auth.LoginResponse
import top.human01d.lobbymanager.security.JwtService
import java.util.*

@Service
class AuthenticationService(
    private val jwtService: JwtService,
) {
    fun login(): LoginResponse {
        val userId = UUID.randomUUID().toString()
        val jwtToken = jwtService.generateToken(userId)
        return LoginResponse(jwtToken)
    }
}
