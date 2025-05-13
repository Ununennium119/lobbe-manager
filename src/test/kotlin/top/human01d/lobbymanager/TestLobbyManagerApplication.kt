package top.human01d.lobbymanager

import org.springframework.boot.fromApplication


fun main(args: Array<String>) {
    fromApplication<LobbyManagerApplication>().with(TestcontainersConfiguration::class.java).run(*args)
}
