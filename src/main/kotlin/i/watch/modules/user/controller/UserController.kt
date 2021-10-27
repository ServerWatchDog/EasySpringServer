package i.watch.modules.user.controller

import i.watch.handler.inject.page.RestPage
import i.watch.handler.security.encrypt.CryptRequestBody
import i.watch.handler.security.session.Permission
import i.watch.modules.user.UserAuthority
import i.watch.modules.user.model.view.user.UserInsertView
import i.watch.modules.user.service.IUserService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${global.server.api}/admin/user")
class UserController(
    private val userService: IUserService
) {
    @Permission("user", [UserAuthority.USER_ADMIN_READ])
    @GetMapping("")
    fun getAllUsers(@RestPage pageable: Pageable) = userService.select(pageable)

    @Permission("user", [UserAuthority.USER_ADMIN_WRITE])
    @PostMapping("")
    fun insert(@CryptRequestBody user: UserInsertView) = userService.insert(user)

    @Permission("user", [UserAuthority.USER_ADMIN_WRITE])
    @PutMapping("{id}")
    fun update(
        @PathVariable("id") userId: Long,
        @CryptRequestBody user: UserInsertView
    ) = userService.update(userId, user)

    @Permission("user", [UserAuthority.USER_ADMIN_WRITE])
    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") userId: Long) = userService.delete(userId)
}
