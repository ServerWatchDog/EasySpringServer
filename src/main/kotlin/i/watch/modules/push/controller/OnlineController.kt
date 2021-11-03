package i.watch.modules.push.controller

import i.watch.handler.inject.session.Permission
import i.watch.handler.inject.session.Session
import i.watch.modules.push.service.IPushService
import i.watch.modules.user.service.impl.UserSessionServiceImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Permission("user")
@RestController
@RequestMapping("\${global.server.api}/online")
class OnlineController(private val pushService: IPushService) {

    @Permission("user")
    @GetMapping("")
    fun getOnline(@Session userSession: UserSessionServiceImpl.UserSession) = pushService.getOnline(userSession)
}
