package top.human01d.lobbymanager

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection(name = "postgres")
    fun postgresContainer(): PostgreSQLContainer<*> {
        return PostgreSQLContainer("postgres:15.13")
            .withDatabaseName("lobby_manager")
            .withUsername("user")
            .withPassword("pass")
    }
}
