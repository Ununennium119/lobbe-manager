package top.human01d.lobbymanager

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import top.human01d.lobbymanager.service.AuthenticationService
import kotlin.test.assertTrue

class AuthenticationServiceTest(
    private val service: AuthenticationService,
) : LobbyManagerBaseTest() {
    @Test
    fun `login should return a token`() {
        val response = service.login()
        val jwtToken = response.token

        val jwtParts = jwtToken.split(".")
        assertEquals(3, jwtParts.size, "JWT should have 3 parts: header, payload, signature")

        assertTrue(jwtToken.length > 50, "JWT token length should be greater than 50")
    }
}
