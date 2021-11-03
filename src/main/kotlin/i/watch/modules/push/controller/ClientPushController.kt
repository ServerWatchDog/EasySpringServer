package i.watch.modules.push.controller

import i.watch.handler.inject.session.Permission
import i.watch.handler.inject.session.Session
import i.watch.modules.push.model.view.push.ServerStatusView
import i.watch.modules.push.service.IPushService
import i.watch.modules.push.service.impl.PushSessionServiceImpl
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("push", ["push-data"])
@RestController
@RequestMapping("\${global.server.api}/push")
class ClientPushController(
    private val pushService: IPushService
) {
    @Permission("push", ["push-data"])
    @PostMapping("")
    fun push(
        @Session pushSession: PushSessionServiceImpl.PushSession,
        @RequestBody serverStatusView: ServerStatusView
    ) =
        pushService.push(pushSession, serverStatusView)
}
