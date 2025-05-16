package top.human01d.lobbymanager.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import top.human01d.lobbymanager.model.User
import java.time.Instant
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.createdAt < :cutoff")
    fun deleteExpiredUsers(cutoff: Instant): Int
}
