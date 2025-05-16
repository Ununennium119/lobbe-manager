package top.human01d.lobbymanager.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import top.human01d.lobbymanager.repository.UserRepository
import java.time.Instant

@Service
class UserCleanupService(
    private val userRepository: UserRepository,

    @Value("\${jwt.expiration-seconds:3600}")
    private val expirationSeconds: Long,
) {
    private val log = KotlinLogging.logger {}


    @Scheduled(fixedRateString = "\${lobby-manager.user-cleanup-interval-ms}")
    fun cleanUpInactiveLobbies() {
        val cutoff = Instant.now().minusSeconds(expirationSeconds)
        val deleted = userRepository.deleteExpiredUsers(cutoff)
        if (deleted > 0) {
            log.info { "Cleaned up $deleted expired users." }
        }
    }
}
