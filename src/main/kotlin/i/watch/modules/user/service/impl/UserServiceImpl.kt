package i.watch.modules.user.service.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import i.watch.modules.user.model.view.LoginResultView
import i.watch.modules.user.model.view.LoginView
import i.watch.modules.user.model.view.RegisterResultView
import i.watch.modules.user.model.view.RegisterView
import i.watch.modules.user.repository.UserRepository
import i.watch.modules.user.service.IUserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val jpaQuery: JPAQueryFactory,
    private val userRepository: UserRepository
) : IUserService {
    override fun tryLogin(loginView: LoginView): LoginResultView {
        TODO()
    }

    override fun tryRegister(registerView: RegisterView): RegisterResultView {
        TODO("Not yet implemented")
    }
}
