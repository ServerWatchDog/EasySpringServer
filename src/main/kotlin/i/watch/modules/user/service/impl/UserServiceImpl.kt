package i.watch.modules.user.service.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import i.watch.handler.advice.BadRequestException
import i.watch.modules.user.model.db.QUserEntity
import i.watch.modules.user.model.view.LoginResultView
import i.watch.modules.user.model.view.LoginView
import i.watch.modules.user.model.view.RegisterResultView
import i.watch.modules.user.model.view.RegisterView
import i.watch.modules.user.repository.UserRepository
import i.watch.modules.user.service.IUserService
import i.watch.utils.HashUtils
import i.watch.utils.getLogger
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UserServiceImpl(
    private val jpaQuery: JPAQueryFactory,
    private val userRepository: UserRepository,
    private val hashUtils: HashUtils
) : IUserService {
    private val logger = getLogger()
    override fun tryLogin(loginView: LoginView): LoginResultView {
        return QUserEntity.userEntity.let {
            jpaQuery.selectFrom(it)
                .where(it.email.eq(loginView.account.lowercase())).fetchOne()
                .run { Optional.ofNullable(this) }
        }.orElseThrow {
            logger.debug("未找到邮箱为 {} 的用户.", loginView.account)
            BadRequestException("用户名或密码错误")
        }.let {
            if (hashUtils.verify(loginView.password, it.password)) {
                //  密码匹配
                if (it.twoFactor.isEmpty()) {
                    LoginResultView(LoginResultView.LoginResultType.NEED_2FA, "")
                } else {
                    LoginResultView(LoginResultView.LoginResultType.SUCCESS, "")
                }
            } else {
                throw BadRequestException("用户名或密码错误")
            }
        }
    }

    override fun tryRegister(registerView: RegisterView): RegisterResultView {
        TODO("Not yet implemented")
    }
}
