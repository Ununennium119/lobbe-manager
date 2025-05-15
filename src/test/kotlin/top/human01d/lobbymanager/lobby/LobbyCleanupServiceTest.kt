package top.human01d.lobbymanager.lobby

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Value
import top.human01d.lobbymanager.LobbyManagerBaseTest
import top.human01d.lobbymanager.model.Lobby
import top.human01d.lobbymanager.repository.LobbyRepository
import top.human01d.lobbymanager.service.LobbyCleanupService
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LobbyCleanupServiceTest(
    private val service: LobbyCleanupService,
    private val repository: LobbyRepository,

    @Value("\${lobby-manager.heartbeat-timeout-seconds}")
    private val heartbeatTimeoutSeconds: Long,
) : LobbyManagerBaseTest() {

    @AfterEach
    fun cleanup() {
        repository.deleteAll()
    }


    @Test
    fun `inactive lobbies should be deleted`() {
        val allLobbies = listOf(
            Lobby(
                UUID.randomUUID(),
                "lobby-1",
                true,
                "AAAAAA",
                4,
            ).apply {
                updateHeartbeat(Instant.now().minusSeconds(heartbeatTimeoutSeconds + 1))
            },
            Lobby(
                UUID.randomUUID(),
                "lobby-2",
                true,
                "BBBBBB",
                4,
            ),
            Lobby(
                UUID.randomUUID(),
                "lobby-3",
                true,
                "CCCCCC",
                4,
            ),
            Lobby(
                UUID.randomUUID(),
                "lobby-4",
                true,
                "DDDDDD",
                4,
            ).apply {
                updateHeartbeat(Instant.now().minusSeconds(heartbeatTimeoutSeconds + 1))
            },
        ).map {
            repository.save(it)
        }

        service.cleanUpInactiveLobbies()

        val lobbies = repository.findAll()
        val activeLobbies = allLobbies
            .filter { it.lastHeartbeat.isAfter(Instant.now().minusSeconds(heartbeatTimeoutSeconds)) }
        assertEquals(activeLobbies.size, lobbies.size)
        activeLobbies.forEach { activeLobby ->
            val lobby = lobbies.firstOrNull { it.id == activeLobby.id }
            assertNotNull(lobby)
        }
    }
}
