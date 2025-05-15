package top.human01d.lobbymanager.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class AlreadyInLobbyException : RuntimeException("User is already in a lobby. Try again later.")
