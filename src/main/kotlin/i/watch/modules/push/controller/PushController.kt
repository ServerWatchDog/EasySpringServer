package i.watch.modules.push.controller

import i.watch.handler.inject.session.Permission
import i.watch.handler.inject.session.Session
import i.watch.modules.client.ClientAuthority
import i.watch.modules.client.model.session.ClientSession
import i.watch.modules.push.PushAuthority
import i.watch.modules.push.model.session.PushSession
import i.watch.modules.push.model.view.push.ClientLoginView
import i.watch.modules.push.model.view.push.ClientPushView
import i.watch.modules.push.service.IPushService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${global.server.api}/client")
class PushController(private val pushService: IPushService) {
    @Permission("cli", [ClientAuthority.ENABLED])
    @PostMapping("connect")
    fun connect(
        @RequestBody clientLogin: ClientLoginView,
        @Session clientSession: ClientSession
    ) = pushService.connect(clientLogin, clientSession)

    @Permission("push", [PushAuthority.STABLE])
    @PostMapping("push")
    fun push(
        @RequestBody clientPush: ClientPushView,
        @Session pushSession: PushSession
    ) = pushService.push(clientPush, pushSession)

    @Permission("push", [PushAuthority.STABLE])
    @DeleteMapping("disconnect")
    fun disConnect(@Session pushSession: PushSession) = pushService.disConnect(pushSession)
}
