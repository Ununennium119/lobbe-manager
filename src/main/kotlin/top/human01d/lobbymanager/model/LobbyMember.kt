package top.human01d.lobbymanager.model

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import top.human01d.lobbymanager.dto.lobby.LobbyMemberDto
import java.util.*

@Entity
@Table(
    name = "lobby_member",
    indexes = [
        Index(name = "idx_lobby_member_lobby_id", columnList = "lobby_id"),
        Index(name = "idx_lobby_member_user_id", columnList = "user_id", unique = true),
    ],
)
class LobbyMember(
    lobby: Lobby,
    userId: UUID,
    isOwner: Boolean,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set

    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val lobby: Lobby = lobby

    @Column(nullable = false, updatable = false)
    val userId: UUID = userId

    @Column(nullable = false, updatable = false)
    val isOwner: Boolean = isOwner

    fun toDto(): LobbyMemberDto {
        return LobbyMemberDto(
            id = id,
            userId = userId,
            isOwner = isOwner,
        )
    }
}
