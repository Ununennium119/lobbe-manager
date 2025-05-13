package top.human01d.lobbymanager.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/api/**")
            .allowedHeaders("Authorization", "Content-Type", "Accept")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedOrigins("http://localhost", "https://human01d.top")
            .allowedOriginPatterns("http://localhost:*", "https://human01d.top")
            .allowCredentials(true)
    }
}
