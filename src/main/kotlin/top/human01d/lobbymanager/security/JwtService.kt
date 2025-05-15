package top.human01d.lobbymanager.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val secretKey: String,
    @Value("\${jwt.expiration-seconds:3600}")
    private val expirationSeconds: Long
) {

    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())


    fun generateToken(subject: String, extraClaims: Map<String, Any> = emptyMap()): String {
        val now = Date()
        val expirationDate = Date(now.time + expirationSeconds * 1000)

        return Jwts.builder()
            .claims(extraClaims)
            .subject(subject)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(key)
            .compact()
    }

    fun extractSubject(token: String): String? {
        return extractAllClaims(token)?.subject
    }

    fun extractIssuedAt(token: String): Date? {
        return extractAllClaims(token)?.issuedAt
    }

    fun extractExpiration(token: String): Date? {
        return extractAllClaims(token)?.expiration
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            val claims = extractAllClaims(token)
            claims != null && !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun extractAllClaims(token: String): Claims? {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: JwtException) {
            null
        }
    }
}
