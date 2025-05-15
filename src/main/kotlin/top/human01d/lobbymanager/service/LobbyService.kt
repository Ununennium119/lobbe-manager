package top.human01d.lobbymanager.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import top.human01d.lobbymanager.api.LobbyCreateRequest
import top.human01d.lobbymanager.dto.lobby.LobbyDto
import top.human01d.lobbymanager.api.LobbyJoinRequest
import top.human01d.lobbymanager.exception.*
import top.human01d.lobbymanager.model.Lobby
import top.human01d.lobbymanager.model.LobbyMember
import top.human01d.lobbymanager.repository.LobbyMemberRepository
import top.human01d.lobbymanager.repository.LobbyRepository
import java.time.Instant
import java.util.*

@Service
class LobbyService(
    private val lobbyRepository: LobbyRepository,
    private val lobbyMemberRepository: LobbyMemberRepository,

    @Value("\${lobby-manager.code-generation-retries}")
    private val codeGenerationRetries: Int,
) {
    fun create(request: LobbyCreateRequest, userId: UUID): LobbyDto {
        if (lobbyMemberRepository.existsByUserId(userId)) {
            throw AlreadyInLobbyException()
        }

        val lobby = Lobby(
            id = UUID.randomUUID(),
            name = request.name,
            private = request.private,
            code = generateCode(),
            capacity = request.capacity,
        ).apply {
            addMember(
                LobbyMember(
                    lobby = this,
                    userId = userId,
                    isOwner = true,
                )
            )
        }

        var lobbySaved = false
        for (i in 0 until codeGenerationRetries) {
            try {
                lobbyRepository.save(lobby)
                lobbySaved = true
                break
            } catch (ex: DataIntegrityViolationException) {
                // Duplicate code, retry
                lobby.updateCode(generateCode())
            }
        }
        if (!lobbySaved) {
            throw FailedToGenerateLobbyCodeException()
        }

        return lobby.toDto()
    }

    fun joinPrivate(request: LobbyJoinRequest, userId: UUID): LobbyDto {
        if (lobbyMemberRepository.existsByUserId(userId)) {
            throw AlreadyInLobbyException()
        }

        val lobby = lobbyRepository.findByCode(request.code.uppercase())
            ?: throw LobbyNotFoundException()
        if (lobby.capacity <= lobby.members.size) {
            throw LobbyIsFullException()
        }

        val lobbyMember = LobbyMember(
            lobby = lobby,
            userId = userId,
            isOwner = false,
        )
        lobbyMemberRepository.save(lobbyMember)

        return lobby.toDto()
    }

    fun joinPublic(id: UUID, userId: UUID): LobbyDto {
        if (lobbyMemberRepository.existsByUserId(userId)) {
            throw AlreadyInLobbyException()
        }

        val lobby = lobbyRepository.findByIdOrNull(id)
            ?: throw LobbyNotFoundException()
        if (lobby.private) {
            throw LobbyNotFoundException()
        }
        if (lobby.capacity <= lobby.members.size) {
            throw LobbyIsFullException()
        }

        val lobbyMember = LobbyMember(
            lobby = lobby,
            userId = userId,
            isOwner = false,
        )
        lobbyMemberRepository.save(lobbyMember)

        return lobby.toDto()
    }

    fun quickJoin(userId: UUID): LobbyDto {
        if (lobbyMemberRepository.existsByUserId(userId)) {
            throw AlreadyInLobbyException()
        }

        val publicLobbies = lobbyRepository.findAllByPrivateFalse()
        val notFullLobbies = publicLobbies.filter { it.capacity > it.members.size }
        if (notFullLobbies.isEmpty()) {
            throw NoLobbyFoundException()
        }

        val selectedLobby = notFullLobbies.random()
        val lobbyMember = LobbyMember(
            lobby = selectedLobby,
            userId = userId,
            isOwner = false,
        )
        lobbyMemberRepository.save(lobbyMember)

        return selectedLobby.toDto()
    }

    fun list(): List<LobbyDto> {
        return lobbyRepository.findAllByPrivateFalse()
            .filter { it.capacity > it.members.size }
            .map { it.toDto() }
    }

    fun leave(id: UUID, userId: UUID) {
        val lobby = lobbyRepository.findByIdOrNull(id)
            ?: throw LobbyNotFoundException()

        val lobbyMember = lobby.members.firstOrNull { it.userId == userId }
        if (lobbyMember == null) {
            throw NotInLobbyException()
        }

        if (lobbyMember.isOwner) {
            lobbyRepository.delete(lobby)
        } else {
            lobby.removeMemberByUserId(userId)
            lobbyRepository.save(lobby)
        }
    }

    fun heartbeat(id: UUID, userId: UUID) {
        val lobby = lobbyRepository.findByIdOrNull(id)
            ?: throw LobbyNotFoundException()

        val lobbyMember = lobby.members.firstOrNull { it.userId == userId }
        if (lobbyMember == null) {
            throw NotInLobbyException()
        }
        if (!lobbyMember.isOwner) {
            throw CannotDoAction()
        }

        lobby.updateHeartbeat(Instant.now())
        lobbyRepository.save(lobby)
    }


    private fun generateCode(): String {
        val alphanumerics = ('A'..'Z') + ('0'..'9')
        return (1..6)
            .map { alphanumerics.random() }
            .joinToString("")
    }
}
