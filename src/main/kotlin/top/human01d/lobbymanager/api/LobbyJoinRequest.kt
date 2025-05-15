package top.human01d.lobbymanager.api

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

class LobbyJoinRequest(
    @NotNull
    @Size(min = 6, max = 6, message = "Lobby code should be 6 characters")
    val code: String,
)
