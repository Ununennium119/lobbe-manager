package top.human01d.lobbymanager.service

import org.springframework.stereotype.Service
import top.human01d.lobbymanager.dto.auth.LoginResponse
import top.human01d.lobbymanager.model.User
import top.human01d.lobbymanager.repository.UserRepository
import top.human01d.lobbymanager.security.JwtService
import java.time.Instant
import java.util.*
import javax.crypto.KeyGenerator

@Service
class AuthenticationService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) {
    companion object {
        private const val AES_KEY_SIZE = 256
    }

    fun login(): LoginResponse {
        val userId = UUID.randomUUID()
        val jwtToken = jwtService.generateToken(userId.toString())
        val aesKey = generateAesKey()

        val user = User(
            id = userId,
            aesKey = aesKey,
            createdAt = Instant.now(),
        )
        userRepository.save(user)

        return LoginResponse(
            userId = user.id,
            token = jwtToken,
            aesKey = user.aesKey,
        )
    }


    private fun generateAesKey(): String {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(AES_KEY_SIZE)
        return Base64.getEncoder().encodeToString(keyGen.generateKey().encoded)
    }
}
