package top.human01d.lobbymanager.model

import jakarta.persistence.*
import top.human01d.lobbymanager.dto.lobby.LobbyDto
import java.time.Instant
import java.util.*

@Entity
@Table(
    name = "lobby",
    indexes = [
        Index(name = "idx_lobby_code", columnList = "code", unique = true),
    ],
)
class Lobby(
    id: UUID,
    name: String,
    private: Boolean,
    code: String,
    capacity: Int,
) {
    @Id
    val id: UUID = id

    @Column(length = 64, nullable = false, updatable = false)
    val name: String = name

    @Column(nullable = false, updatable = false)
    val private: Boolean = private

    @Column(length = 6, nullable = false, updatable = false)
    var code: String = code
        private set

    @Column(nullable = false, updatable = false)
    val capacity: Int = capacity

    @OneToMany(
        mappedBy = "lobby",
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER,
        orphanRemoval = true,
    )
    val members: MutableList<LobbyMember> = mutableListOf()

    @Column(nullable = false)
    var lastHeartbeat: Instant = Instant.now()
        private set


    fun addMember(member: LobbyMember) {
        members.add(member)
    }

    fun removeMemberByUserId(userId: UUID) {
        members.removeIf { it.userId == userId }
    }

    fun updateCode(code: String) {
        this.code = code
    }

    fun updateHeartbeat(heartbeat: Instant) {
        lastHeartbeat = heartbeat
    }


    fun toDto(): LobbyDto {
        return LobbyDto(
            id = id,
            name = name,
            private = private,
            code = code,
            capacity = capacity,
            members = members.map { it.toDto() },
        )
    }
}
