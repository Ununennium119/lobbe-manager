package top.human01d.lobbymanager.lobby

import org.junit.jupiter.api.*
import org.springframework.data.repository.findByIdOrNull
import top.human01d.lobbymanager.LobbyManagerBaseTest
import top.human01d.lobbymanager.api.LobbyCreateRequest
import top.human01d.lobbymanager.api.LobbyJoinRequest
import top.human01d.lobbymanager.exception.*
import top.human01d.lobbymanager.repository.LobbyMemberRepository
import top.human01d.lobbymanager.repository.LobbyRepository
import top.human01d.lobbymanager.service.LobbyService
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LobbyServiceTest(
    private val service: LobbyService,
    private val lobbyRepository: LobbyRepository,
    private val lobbyMemberRepository: LobbyMemberRepository,
) : LobbyManagerBaseTest() {

    @AfterEach
    fun cleanup() {
        lobbyRepository.deleteAll()
    }


    @Nested
    @DisplayName("Tests for create()")
    inner class CreateTests {
        @Test
        fun `lobby should be created`() {
            val request = LobbyCreateRequest(
                name = "test-lobby",
                true,
                capacity = 4,
            )
            val userId = UUID.randomUUID()

            val lobbyDto = service.create(request, userId)

            val lobby = lobbyRepository.findByIdOrNull(lobbyDto.id)
            assertNotNull(lobby)
            assertEquals(request.name, lobby.name)
            assertEquals(request.private, lobby.private)
        }

        @Test
        fun `lobby should not be created if user is already in a lobby`() {
            val request = LobbyCreateRequest(
                name = "test-lobby",
                true,
                capacity = 4,
            )
            val userId = UUID.randomUUID()
            service.create(request, userId)

            assertThrows<AlreadyInLobbyException> {
                service.create(request, userId)
            }
        }
    }

    @Nested
    @DisplayName("Tests for joinPrivate()")
    inner class JoinPrivateTests {
        @Test
        fun `user should join private lobby`() {
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    true,
                    capacity = 4,
                ),
                UUID.randomUUID(),
            )
            val userId = UUID.randomUUID()
            val request = LobbyJoinRequest(
                code = lobbyDto.code,
            )

            val joinedLobbyDto = service.joinPrivate(request, userId)

            assertEquals(lobbyDto.id, joinedLobbyDto.id)
            val lobbyMember = lobbyMemberRepository.findByLobbyIdAndUserId(lobbyDto.id, userId)
            assertNotNull(lobbyMember)
        }

        @Test
        fun `user should not join private lobby if user is already in a lobby`() {
            val userId = UUID.randomUUID()
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    true,
                    capacity = 4,
                ),
                userId,
            )
            val request = LobbyJoinRequest(
                code = lobbyDto.code,
            )

            assertThrows<AlreadyInLobbyException> {
                service.joinPrivate(request, userId)
            }
        }

        @Test
        fun `user should not join private lobby if code is invalid`() {
            service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    true,
                    capacity = 4,
                ),
                UUID.randomUUID(),
            )
            val userId = UUID.randomUUID()
            val request = LobbyJoinRequest(
                code = "invalid",
            )

            assertThrows<LobbyNotFoundException> {
                service.joinPrivate(request, userId)
            }
        }

        @Test
        fun `user should not join private lobby if lobby is full`() {
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    true,
                    capacity = 1,
                ),
                UUID.randomUUID(),
            )
            val userId = UUID.randomUUID()
            val request = LobbyJoinRequest(
                code = lobbyDto.code,
            )

            assertThrows<LobbyIsFullException> {
                service.joinPrivate(request, userId)
            }
        }
    }

    @Nested
    @DisplayName("Tests for joinPublic()")
    inner class JoinPublicTests {
        @Test
        fun `user should join public lobby`() {
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 4,
                ),
                UUID.randomUUID(),
            )
            val userId = UUID.randomUUID()

            val joinedLobbyDto = service.joinPublic(lobbyDto.id, userId)

            assertEquals(lobbyDto.id, joinedLobbyDto.id)
            val lobbyMember = lobbyMemberRepository.findByLobbyIdAndUserId(lobbyDto.id, userId)
            assertNotNull(lobbyMember)
        }

        @Test
        fun `user should not join public lobby if user is already in a lobby`() {
            val userId = UUID.randomUUID()
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 4,
                ),
                userId,
            )

            assertThrows<AlreadyInLobbyException> {
                service.joinPublic(lobbyDto.id, userId)
            }
        }

        @Test
        fun `user should not join public lobby if lobby is private`() {
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    true,
                    capacity = 4,
                ),
                UUID.randomUUID(),
            )
            val userId = UUID.randomUUID()

            assertThrows<LobbyNotFoundException> {
                service.joinPublic(lobbyDto.id, userId)
            }
        }

        @Test
        fun `user should not join public lobby if lobby is full`() {
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 1,
                ),
                UUID.randomUUID(),
            )
            val userId = UUID.randomUUID()

            assertThrows<LobbyIsFullException> {
                service.joinPublic(lobbyDto.id, userId)
            }
        }
    }

    @Nested
    @DisplayName("Tests for quickJoin()")
    inner class QuickJoinTests {
        @Test
        fun `user should quick join lobby`() {
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 4,
                ),
                UUID.randomUUID(),
            )
            val userId = UUID.randomUUID()

            val joinedLobbyDto = service.quickJoin(userId)

            assertEquals(lobbyDto.id, joinedLobbyDto.id)
            val lobbyMember = lobbyMemberRepository.findByLobbyIdAndUserId(lobbyDto.id, userId)
            assertNotNull(lobbyMember)
        }

        @Test
        fun `user should not quick join lobby if user is already in a lobby`() {
            val userId = UUID.randomUUID()
            service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 4,
                ),
                userId,
            )

            assertThrows<AlreadyInLobbyException> {
                service.quickJoin(userId)
            }
        }

        @Test
        fun `user should not quick join lobby if there is no public not full lobby`() {
            service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    true,
                    capacity = 4,
                ),
                UUID.randomUUID(),
            )
            service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 1,
                ),
                UUID.randomUUID(),
            )
            val userId = UUID.randomUUID()

            assertThrows<NoLobbyFoundException> {
                service.quickJoin(userId)
            }
        }
    }

    @Nested
    @DisplayName("Tests for list()")
    inner class ListTests {
        @Test
        fun `list should return all public not full lobbies`() {
            val createdLobbyDtoList = listOf(
                LobbyCreateRequest(
                    name = "test-lobby-1",
                    true,
                    capacity = 4,
                ),
                LobbyCreateRequest(
                    name = "test-lobby-2",
                    false,
                    capacity = 4,
                ),
                LobbyCreateRequest(
                    name = "test-lobby-3",
                    false,
                    capacity = 4,
                ),
                LobbyCreateRequest(
                    name = "test-lobby-3",
                    false,
                    capacity = 1,
                )
            ).map {
                service.create(
                    it,
                    UUID.randomUUID(),
                )
            }

            val lobbyListDto = service.list()

            val publicNotFullLobbyDtoList =
                createdLobbyDtoList.filter { !it.private }
                    .filter { it.capacity > it.members.size }
            assertEquals(publicNotFullLobbyDtoList.size, lobbyListDto.size)
            publicNotFullLobbyDtoList.forEach { publicLobby ->
                val lobby = lobbyListDto.firstOrNull { it.id == publicLobby.id }
                assertNotNull(lobby)
            }
        }
    }

    @Nested
    @DisplayName("Tests for leave()")
    inner class LeaveTests {
        @Test
        fun `user should leave lobby`() {
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 4,
                ),
                UUID.randomUUID(),
            )
            val userId = UUID.randomUUID()
            service.joinPublic(lobbyDto.id, userId)

            service.leave(lobbyDto.id, userId)

            val lobbyMember = lobbyMemberRepository.findByLobbyIdAndUserId(lobbyDto.id, userId)
            assertNull(lobbyMember)
        }

        @Test
        fun `owner should leave and delete lobby`() {
            val userId = UUID.randomUUID()
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 4,
                ),
                userId,
            )

            service.leave(lobbyDto.id, userId)

            val lobby = lobbyRepository.findByIdOrNull(lobbyDto.id)
            assertNull(lobby)
        }

        @Test
        fun `user should not leave lobby if is not in lobby`() {
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 4,
                ),
                UUID.randomUUID(),
            )
            val userId = UUID.randomUUID()

            assertThrows<NotInLobbyException> {
                service.leave(lobbyDto.id, userId)
            }
        }
    }

    @Nested
    @DisplayName("Tests for heartbeat()")
    inner class HeartbeatTests {
        @Test
        fun `last heartbeat should be updated`() {
            val userId = UUID.randomUUID()
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 4,
                ),
                userId,
            )

            val beforeHeartbeat = Instant.now()
            service.heartbeat(lobbyDto.id, userId)
            val afterHeartbeat = Instant.now()

            val lobby = lobbyRepository.findByIdOrNull(lobbyDto.id)!!
            assertTrue { lobby.lastHeartbeat.toEpochMilli() in (beforeHeartbeat.toEpochMilli()..afterHeartbeat.toEpochMilli()) }
        }

        @Test
        fun `last heartbeat should not be updated if user is not in the lobby`() {
            val userId = UUID.randomUUID()
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 4,
                ),
                userId,
            )

            assertThrows<NotInLobbyException> {
                service.heartbeat(lobbyDto.id, UUID.randomUUID())
            }
        }

        @Test
        fun `last heartbeat should not be updated if user is not the owner`() {
            val lobbyDto = service.create(
                LobbyCreateRequest(
                    name = "test-lobby",
                    false,
                    capacity = 4,
                ),
                UUID.randomUUID(),
            )
            val userId = UUID.randomUUID()
            service.joinPublic(lobbyDto.id, userId)

            assertThrows<CannotDoAction> {
                service.heartbeat(lobbyDto.id, userId)
            }
        }
    }
}
