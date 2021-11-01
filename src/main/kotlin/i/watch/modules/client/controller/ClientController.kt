package i.watch.modules.client.controller

import i.watch.handler.inject.session.Permission
import i.watch.handler.inject.session.Session
import i.watch.modules.client.ClientAuthority
import i.watch.modules.client.model.view.client.ClientInfoView
import i.watch.modules.client.service.IClientService
import i.watch.modules.client.service.impl.ClientSession
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${global.server.api}/client")
class ClientController(private val clientService: IClientService) {

    @Permission("cli", [ClientAuthority.ENABLED])
    @PostMapping("/login")
    fun login(@Session clientSession: ClientSession, @RequestBody clientInfo: ClientInfoView) =
        clientService.clientLogin(clientSession, clientInfo)
}
