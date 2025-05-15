package top.human01d.lobbymanager.api

import top.human01d.lobbymanager.dto.lobby.LobbyDto
import java.util.*

class LobbyCreateResponse(lobby: LobbyDto) {
    val id: UUID = lobby.id
    val code: String = lobby.code
}
