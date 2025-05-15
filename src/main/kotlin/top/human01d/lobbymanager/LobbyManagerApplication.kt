package top.human01d.lobbymanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(
    value = [
        LobbyManagerConfiguration::class,
    ]
)
@SpringBootApplication
class LobbyManagerApplication

fun main(args: Array<String>) {
    runApplication<LobbyManagerApplication>(*args)
}
