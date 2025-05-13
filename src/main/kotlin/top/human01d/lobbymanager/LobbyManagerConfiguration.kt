package top.human01d.lobbymanager

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import top.human01d.lobbymanager.configuration.SecurityConfiguration
import top.human01d.lobbymanager.configuration.WebMvcConfiguration

@Configuration
@Import(
    value = [
        SecurityConfiguration::class,
        WebMvcConfiguration::class,
    ]
)
class LobbyManagerConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(
                kotlinModule {
                    configure(KotlinFeature.NullIsSameAsDefault, true)
                    configure(KotlinFeature.KotlinPropertyNameAsImplicitName, true)
                }
            )
    }
}
