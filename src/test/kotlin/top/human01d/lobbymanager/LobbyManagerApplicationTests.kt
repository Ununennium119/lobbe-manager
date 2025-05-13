package top.human01d.lobbymanager

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class LobbyManagerApplicationTests {

	@Test
	fun contextLoads() {
	}

}
