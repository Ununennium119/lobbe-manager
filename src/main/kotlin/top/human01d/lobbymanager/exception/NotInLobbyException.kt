package top.human01d.lobbymanager.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class NotInLobbyException : RuntimeException("User is not in the lobby.")
