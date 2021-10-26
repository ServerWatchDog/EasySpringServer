package i.watch.modules.user.service

import i.watch.modules.user.model.view.LoginResultView
import i.watch.modules.user.model.view.LoginView
import i.watch.modules.user.model.view.RegisterResultView
import i.watch.modules.user.model.view.RegisterView

interface IUserService {
    /**
     * 尝试登录
     */
    fun tryLogin(loginView: LoginView): LoginResultView
    fun tryRegister(registerView: RegisterView): RegisterResultView
    fun getUserAuthorities(userId: Long): List<String>
}
