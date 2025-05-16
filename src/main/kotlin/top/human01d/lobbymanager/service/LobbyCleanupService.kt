package top.human01d.lobbymanager.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import top.human01d.lobbymanager.repository.LobbyRepository
import java.time.Instant

@Service
class LobbyCleanupService(
    private val lobbyRepository: LobbyRepository,

    @Value("\${lobby-manager.heartbeat-timeout-seconds}")
    private val heartbeatTimeoutSeconds: Long,
) {
    private val log = KotlinLogging.logger {}


    @Scheduled(fixedRateString = "\${lobby-manager.lobby-cleanup-interval-ms}")
    fun cleanUpInactiveLobbies() {
        val cutoff = Instant.now().minusSeconds(heartbeatTimeoutSeconds)
        val deleted = lobbyRepository.deleteInactiveLobbies(cutoff)
        if (deleted > 0) {
            log.info { "Cleaned up $deleted inactive lobbies." }
        }
    }
}
