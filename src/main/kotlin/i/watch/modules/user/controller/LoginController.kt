package i.watch.modules.user.controller

import i.watch.modules.user.model.view.login.LoginView
import i.watch.modules.user.model.view.register.RegisterView
import i.watch.modules.user.service.IUserService
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    private val userService: IUserService
) : LoginApi {
    override fun login(
        loginView: LoginView
    ) = userService.tryLogin(loginView)

    override fun register(
        registerView: RegisterView
    ) = userService.tryRegister(registerView)
}
