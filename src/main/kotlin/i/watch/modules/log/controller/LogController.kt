package i.watch.modules.log.controller

import i.watch.handler.inject.page.RestPage
import i.watch.handler.inject.session.Permission
import i.watch.modules.log.service.ILogPushService
import i.watch.modules.user.UserAuthority
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/log")
class LogController(
    private val logService: ILogPushService
) {
    @Permission("user", [UserAuthority.USER_ADMIN_READ])
    @GetMapping("/user/{userId}/{date}")
    fun getUserLog(
        @RestPage page: Pageable,
        @PathVariable("userId") id: Long,
        @PathVariable("date") date: String
    ) = logService.getUserLog(page, id, date)

    @Permission("user", [UserAuthority.CLIENT_ADMIN_READ])
    @GetMapping("/client/{clientId}/{date}")
    fun getClientLog(
        @RestPage page: Pageable,
        @PathVariable("clientId") id: Long,
        @PathVariable("date") date: String
    ) = logService.getClientLog(page, id, date)
}
