package top.human01d.lobbymanager.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import top.human01d.lobbymanager.model.Lobby
import java.time.Instant
import java.util.*

@Repository
interface LobbyRepository : JpaRepository<Lobby, UUID> {

    fun findByCode(code: String): Lobby?

    @Modifying
    @Transactional
    @Query("DELETE FROM Lobby l WHERE l.lastHeartbeat < :cutoff")
    fun deleteInactiveLobbies(cutoff: Instant): Int

    fun findAllByPrivateFalse(): List<Lobby>
}
