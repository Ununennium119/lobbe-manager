package top.human01d.lobbymanager.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class LobbyIsFullException : RuntimeException("Lobby is full")
