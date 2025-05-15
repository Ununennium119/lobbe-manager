package top.human01d.lobbymanager.dto.lobby

import java.util.*

data class LobbyDto(
    val id: UUID,
    val name: String,
    val private: Boolean,
    val code: String,
    val capacity: Int,
    val members: List<LobbyMemberDto>
)
