package i.watch.modules.user.service.impl

import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil
import com.querydsl.jpa.impl.JPAQueryFactory
import i.watch.handler.advice.BadRequestException
import i.watch.modules.user.config.UserConfig
import i.watch.modules.user.model.db.QAuthorityEntity
import i.watch.modules.user.model.db.QGroupEntity
import i.watch.modules.user.model.db.QUserEntity
import i.watch.modules.user.model.view.LoginResultView
import i.watch.modules.user.model.view.LoginView
import i.watch.modules.user.model.view.RegisterResultView
import i.watch.modules.user.model.view.RegisterView
import i.watch.modules.user.service.IUserService
import i.watch.modules.user.service.IUserSessionService
import i.watch.utils.HashUtils
import i.watch.utils.getLogger
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import java.util.Optional
import javax.annotation.Resource

@Service
class UserServiceImpl(
    private val jpaQuery: JPAQueryFactory,
    private val hashUtils: HashUtils,
    private val applicationContext: ApplicationContext
) : IUserService {
    @Resource
    private lateinit var userConfig: UserConfig

    @Resource
    private lateinit var userSessionService: IUserSessionService

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
                    LoginResultView(LoginResultView.LoginResultType.SUCCESS, "")
                } else {
                    // 两步验证
                    if (TimeBasedOneTimePasswordUtil.generateCurrentNumberString(it.twoFactor)
                        == loginView.code
                    ) {
                        LoginResultView(
                            LoginResultView.LoginResultType.SUCCESS,
                            userSessionService.createSessionByUserId(it.id)
                        )
                    } else {
                        LoginResultView(LoginResultView.LoginResultType.NEED_2FA, "")
                    }
                }
            } else {
                throw BadRequestException("用户名或密码错误")
            }
        }
    }

    override fun tryRegister(registerView: RegisterView): RegisterResultView {
        println(userConfig.defaultUserId)
        TODO()
    }

    override fun getUserAuthorities(userId: Long): List<String> {
        val authorityEntity = QAuthorityEntity.authorityEntity
        val userEntity = QUserEntity.userEntity
        val groupEntity = QGroupEntity.groupEntity
        return jpaQuery.select(authorityEntity.id)
            .from(userEntity)
            .leftJoin(groupEntity)
            .on(userEntity.linkGroup.id.eq(groupEntity.id))
            .on(userEntity.id.eq(userId))
            .join(groupEntity.linkedAuthorities, authorityEntity)
            .groupBy(authorityEntity.id)
            .fetch()
    }
}
