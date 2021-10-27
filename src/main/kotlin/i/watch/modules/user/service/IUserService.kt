package i.watch.modules.user.service

import i.watch.modules.user.model.view.login.LoginResultView
import i.watch.modules.user.model.view.login.LoginView
import i.watch.modules.user.model.view.register.RegisterResultView
import i.watch.modules.user.model.view.register.RegisterView
import i.watch.modules.user.model.view.user.UserInsertView
import i.watch.modules.user.model.view.user.UserResultView
import i.watch.modules.user.model.view.user.UsersResultView
import i.watch.utils.SimpleResultView
import org.springframework.data.domain.Pageable

interface IUserService {
    /**
     * 尝试登录
     */
    fun tryLogin(loginView: LoginView): LoginResultView
    fun tryRegister(registerView: RegisterView): RegisterResultView
    fun getUserAuthorities(userId: Long): List<String>
    fun select(pageable: Pageable): UsersResultView
    fun insert(user: UserInsertView): UserResultView
    fun update(userId: Long, user: UserInsertView): UserResultView
    fun delete(userId: Long): SimpleResultView<Boolean>
}
