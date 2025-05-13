package top.human01d.lobbymanagement

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<LobbyManagementApplication>().with(TestcontainersConfiguration::class).run(*args)
}
