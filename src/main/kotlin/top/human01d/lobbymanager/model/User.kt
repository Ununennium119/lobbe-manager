package top.human01d.lobbymanager.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(
    name = "lobby_user",
)
class User(
    id: UUID,
    aesKey: String,
    createdAt: Instant,
) {
    @Id
    val id: UUID = id

    @Column(length = 256, nullable = false, updatable = false)
    val aesKey: String = aesKey

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = createdAt
}
