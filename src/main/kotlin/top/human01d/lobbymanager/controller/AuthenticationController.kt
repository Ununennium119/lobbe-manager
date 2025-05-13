package top.human01d.lobbymanager.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.human01d.lobbymanager.dto.auth.LoginResponse
import top.human01d.lobbymanager.service.AuthenticationService

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val service: AuthenticationService,
) {
    @PostMapping("/login", produces = [MediaType.APPLICATION_JSON_VALUE])
    private fun login(): ResponseEntity<LoginResponse> {
        val response = service.login()
        return ResponseEntity.ok(response)
    }
}
