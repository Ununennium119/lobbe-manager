package top.human01d.lobbymanager.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
class FailedToGenerateLobbyCodeException :
    RuntimeException("Could not generate a unique lobby code. Please try again later.")
