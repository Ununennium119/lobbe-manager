package top.human01d.lobbymanager.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class LobbyNotFoundException : RuntimeException("Lobby not found")
