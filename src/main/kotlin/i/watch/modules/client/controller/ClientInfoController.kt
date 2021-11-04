package i.watch.modules.client.controller

import i.watch.handler.inject.session.Permission
import i.watch.handler.inject.session.Session
import i.watch.modules.client.service.IClientInfoService
import i.watch.modules.user.UserAuthority
import i.watch.modules.user.service.impl.UserSessionServiceImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("\${global.server.api}/view/client")
@RestController
class ClientInfoController(
    private val clientInfoService: IClientInfoService
) {
    @GetMapping("/online")
    @Permission("user", [UserAuthority.CLIENT_ADMIN_WRITE])
    fun getOnline(@Session userSession: UserSessionServiceImpl.UserSession) =
        clientInfoService.currentOnline(userSession)
}
