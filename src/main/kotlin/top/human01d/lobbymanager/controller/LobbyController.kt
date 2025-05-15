package top.human01d.lobbymanager.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import top.human01d.lobbymanager.api.*
import top.human01d.lobbymanager.security.UserContext
import top.human01d.lobbymanager.service.LobbyService
import java.util.*

@RestController
@RequestMapping("/api/lobby")
class LobbyController(
    private val service: LobbyService,
) {
    @PostMapping(
        value = [""],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun create(@RequestBody request: LobbyCreateRequest): ResponseEntity<LobbyCreateResponse> {
        val userId = getUserId() ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)
        val dto = service.create(request, userId)
        return ResponseEntity.ok(LobbyCreateResponse(dto))
    }

    @PostMapping(
        value = ["/join"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun joinPrivate(
        @RequestBody request: LobbyJoinRequest,
    ): ResponseEntity<LobbyJoinResponse> {
        val userId = getUserId() ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)
        val dto = service.joinPrivate(request, userId)
        return ResponseEntity.ok(LobbyJoinResponse(dto))
    }

    @PostMapping(
        value = ["/{id}/join"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun joinPublic(
        @PathVariable id: UUID,
    ): ResponseEntity<LobbyJoinResponse> {
        val userId = getUserId() ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)
        val dto = service.joinPublic(id, userId)
        return ResponseEntity.ok(LobbyJoinResponse(dto))
    }

    @PostMapping(
        value = ["/quick-join"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun quickJoin(): ResponseEntity<LobbyJoinResponse> {
        val userId = getUserId() ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)
        val dto = service.quickJoin(userId)
        return ResponseEntity.ok(LobbyJoinResponse(dto))
    }

    @GetMapping(
        value = ["/"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun list(): ResponseEntity<List<LobbyListResponse>> {
        val dtoList = service.list()
        return ResponseEntity.ok(dtoList.map { LobbyListResponse(it) })
    }

    @PostMapping(
        value = ["/{id}/leave"],
    )
    fun leave(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        val userId = getUserId() ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)
        service.leave(id, userId)
        return ResponseEntity.ok().build()
    }

    @PostMapping(
        value = ["/{id}/heartbeat"],
    )
    fun heartbeat(
        @PathVariable id: UUID,
    ): ResponseEntity<Void> {
        val userId = getUserId() ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)
        service.heartbeat(id, userId)
        return ResponseEntity.ok().build()
    }


    private fun getUserId(): UUID? {
        return UserContext.get()
    }
}
