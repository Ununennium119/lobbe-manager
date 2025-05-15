package top.human01d.lobbymanager.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import top.human01d.lobbymanager.security.JwtService
import top.human01d.lobbymanager.security.UserContext
import java.util.*

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                val token = authHeader.removePrefix("Bearer ")
                if (jwtService.isTokenValid(token)) {
                    val userIdString = jwtService.extractSubject(token)
                    val userId = UUID.fromString(userIdString)
                    UserContext.set(userId)
                }
            } catch (exception: Exception) {
                response.status = HttpStatus.UNAUTHORIZED.value()
            }
        }
        filterChain.doFilter(request, response)
    }
}
