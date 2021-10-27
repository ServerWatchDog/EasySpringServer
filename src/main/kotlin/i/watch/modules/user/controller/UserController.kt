package i.watch.modules.user.controller

import i.watch.handler.inject.page.RestPage
import i.watch.handler.security.session.Permission
import i.watch.modules.user.UserAuthority
import i.watch.modules.user.service.IUserService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${global.server.api}/admin/user")
class UserController(
    private val userService: IUserService
) {
    @Permission("user", [UserAuthority.USER_ADMIN_READ])
    @GetMapping("")
    fun getAllUsers(@RestPage pageable: Pageable) = userService.getAllUsers(pageable)
}
