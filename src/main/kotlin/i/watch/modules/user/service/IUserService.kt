package i.watch.modules.user.service

import i.watch.modules.user.model.view.login.LoginResultView
import i.watch.modules.user.model.view.login.LoginView
import i.watch.modules.user.model.view.register.RegisterResultView
import i.watch.modules.user.model.view.register.RegisterView
import i.watch.modules.user.model.view.user.UserInfoResultView
import i.watch.modules.user.model.view.user.UserInsertView
import i.watch.modules.user.model.view.user.UserResultView
import i.watch.modules.user.service.impl.UserSessionServiceImpl
import i.watch.utils.template.crud.CRUDService

interface IUserService : CRUDService<UserInsertView, UserResultView, Long> {
    /**
     * 尝试登录
     */
    fun tryLogin(loginView: LoginView): LoginResultView
    fun tryRegister(user: RegisterView): RegisterResultView
    fun getUserAuthorities(userId: Long): List<String>
    fun userInfo(userSession: UserSessionServiceImpl.UserSession): UserInfoResultView
}
