package top.human01d.lobbymanager

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import top.human01d.lobbymanager.service.JwtService
import java.util.*
import kotlin.test.*

class JwtServiceTest(
    private val jwtService: JwtService,
    @Value("\${jwt.expiration-seconds:3600}")
    private val expirationSeconds: Long
) : LobbyManagerBaseTest() {

    private val subject = "subject"
    private val extraClaims = mapOf(
        "key-1" to "value-1",
        "key-2" to "value-2"
    )
    private val invalidJwtToken = "invalid-jwt-token"
    private lateinit var jwtToken: String
    private lateinit var beforeGeneration: Date
    private lateinit var afterGeneration: Date


    @BeforeAll
    fun setupAll() {
        beforeGeneration = Date()
        jwtToken = jwtService.generateToken(subject, extraClaims)
        afterGeneration = Date()
    }


    @Test
    fun `token should be valid`() {
        assertTrue(jwtService.isTokenValid(jwtToken))
    }

    @Test
    fun `subject should be correct`() {
        assertEquals(subject, jwtService.extractSubject(jwtToken))
    }

    @Test
    fun `issuedAt should be correct`() {
        val issuedAt = jwtService.extractIssuedAt(jwtToken)
        assertNotNull(issuedAt)
        assertTrue {
            issuedAt.time / 1000 in (beforeGeneration.time / 1000..afterGeneration.time / 1000)
        }
    }

    @Test
    fun `expiration should be correct`() {
        val expiration = jwtService.extractExpiration(jwtToken)
        assertNotNull(expiration)
        val expectedMin = beforeGeneration.time / 1000 + expirationSeconds
        val expectedMax = afterGeneration.time / 1000 + expirationSeconds
        assertTrue(expiration.time / 1000 in expectedMin..expectedMax)
    }

    @Test
    fun `extra claims should be correct`() {
        val claims = jwtService.extractAllClaims(jwtToken)
        assertNotNull(claims)
        for ((key, value) in extraClaims) {
            assertTrue(claims.containsKey(key))
            assertEquals(value, claims[key])
        }
    }

    @Test
    fun `token should be invalid`() {
        assertFalse { jwtService.isTokenValid(invalidJwtToken) }
    }

    @Test
    fun `subject should be null`() {
        assertNull(jwtService.extractSubject(invalidJwtToken))
    }
}
