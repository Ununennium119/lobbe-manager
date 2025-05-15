package top.human01d.lobbymanager.api

import top.human01d.lobbymanager.dto.lobby.LobbyDto
import java.util.*

class LobbyJoinResponse(lobby: LobbyDto) {
    val id: UUID = lobby.id
    val name: String = lobby.name
}
