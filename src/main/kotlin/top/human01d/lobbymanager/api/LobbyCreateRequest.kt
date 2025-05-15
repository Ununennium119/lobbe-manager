package top.human01d.lobbymanager.api

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

class LobbyCreateRequest(
    @NotNull
    @Size(min = 1, max = 63, message = "Lobby name must be between 1 and 63")
    val name: String,

    @NotNull
    val private: Boolean,

    @NotNull
    @Min(1, message = "Lobby capacity should be a positive integer")
    val capacity: Int,
)
