package top.human01d.lobbymanager.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import top.human01d.lobbymanager.model.LobbyMember
import java.util.*

@Repository
interface LobbyMemberRepository : JpaRepository<LobbyMember, Long> {

    fun existsByUserId(userId: UUID): Boolean

    fun findByLobbyIdAndUserId(lobbyId: UUID, userId: UUID): LobbyMember?
}
