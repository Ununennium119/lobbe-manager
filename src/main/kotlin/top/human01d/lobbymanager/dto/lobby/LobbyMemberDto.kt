package top.human01d.lobbymanager.dto.lobby

import java.util.*

data class LobbyMemberDto(
    val id: Long?,
    val userId: UUID,
    val isOwner: Boolean,
)
