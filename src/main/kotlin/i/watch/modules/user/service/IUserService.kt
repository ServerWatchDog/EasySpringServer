package i.watch.modules.user.service

import i.watch.modules.user.model.view.login.LoginResultView
import i.watch.modules.user.model.view.login.LoginView
import i.watch.modules.user.model.view.register.RegisterResultView
import i.watch.modules.user.model.view.register.RegisterView
import i.watch.modules.user.model.view.user.AllUsersResultView
import org.springframework.data.domain.Pageable

interface IUserService {
    /**
     * 尝试登录
     */
    fun tryLogin(loginView: LoginView): LoginResultView
    fun tryRegister(registerView: RegisterView): RegisterResultView
    fun getUserAuthorities(userId: Long): List<String>
    fun getAllUsers(pageable: Pageable): AllUsersResultView
}
