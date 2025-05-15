package top.human01d.lobbymanager.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class CannotDoAction : RuntimeException("You cannot do this action.")
