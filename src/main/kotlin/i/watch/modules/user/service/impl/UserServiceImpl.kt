package i.watch.modules.user.service.impl

import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil
import com.querydsl.jpa.impl.JPAQueryFactory
import i.watch.handler.advice.BadRequestException
import i.watch.handler.advice.ForbiddenException
import i.watch.modules.user.config.UserConfig
import i.watch.modules.user.model.db.QAuthorityEntity
import i.watch.modules.user.model.db.QGroupEntity
import i.watch.modules.user.model.db.QUserEntity
import i.watch.modules.user.model.db.UserEntity
import i.watch.modules.user.model.view.login.LoginResultView
import i.watch.modules.user.model.view.login.LoginView
import i.watch.modules.user.model.view.register.RegisterResultView
import i.watch.modules.user.model.view.register.RegisterView
import i.watch.modules.user.model.view.user.UserInsertView
import i.watch.modules.user.model.view.user.UserResultView
import i.watch.modules.user.model.view.user.UsersResultView
import i.watch.modules.user.repository.UserRepository
import i.watch.modules.user.service.IUserService
import i.watch.modules.user.service.IUserSessionService
import i.watch.utils.HashUtils
import i.watch.utils.SimpleResultView
import i.watch.utils.SnowFlakeUtils
import i.watch.utils.cache.cache.IDataCacheManager
import i.watch.utils.cache.cache.cache
import i.watch.utils.getLogger
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import javax.annotation.Resource

@Service
class UserServiceImpl(
    private val jpaQuery: JPAQueryFactory,
    private val userRepository: UserRepository,
    private val hashUtils: HashUtils,
    private val userConfig: UserConfig,
    private val idGenerator: SnowFlakeUtils,
    private val dataCacheManager: IDataCacheManager
) : IUserService {

    private val authorityCache = dataCacheManager.newCache("authority")

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
                    LoginResultView(
                        LoginResultView.LoginResultType.SUCCESS,
                        userSessionService.createSessionByUserId(it.id)
                    )
                } else {
                    // 两步验证
                    if (TimeBasedOneTimePasswordUtil.generateCurrentNumberString(it.twoFactor)
                        == loginView.code
                    ) {
                        LoginResultView(
                            // 登录成功
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
        if (!userConfig.allowRegister) {
            throw BadRequestException("管理员配置不允许注册.")
        }
        val exists = QUserEntity.userEntity.let {
            jpaQuery.selectFrom(it)
                .where(it.email.eq(registerView.email.lowercase())).fetchCount() != 0L
        }
        if (exists) {
            throw ForbiddenException("此用户已被注册.")
        }
        val nextUuid = idGenerator.nextId()
        val group = jpaQuery
            .selectFrom(QGroupEntity.groupEntity)
            .where(QGroupEntity.groupEntity.id.eq(userConfig.defaultGroupId))
            .fetchOne() ?: throw BadRequestException("管理员配置不允许注册.")
        userRepository.saveAndFlush(
            UserEntity(
                id = nextUuid,
                email = registerView.email,
                name = registerView.name,
                password = hashUtils.create(registerView.password),
                twoFactor = "",
                registerTime = LocalDateTime.now(),
                linkGroup = group
            )
        )
        return RegisterResultView(status = RegisterResultView.RegisterStatus.SUCCESS)
    }

    override fun getUserAuthorities(userId: Long): List<String> {
        return authorityCache.cache(userId.toString()) {
            val authorityEntity = QAuthorityEntity.authorityEntity
            val userEntity = QUserEntity.userEntity
            val groupEntity = QGroupEntity.groupEntity
            jpaQuery.select(authorityEntity.id)
                .from(userEntity)
                .leftJoin(groupEntity)
                .on(userEntity.linkGroup.id.eq(groupEntity.id))
                .on(userEntity.id.eq(userId))
                .join(groupEntity.linkedAuthorities, authorityEntity)
                .groupBy(authorityEntity.id)
                .fetch()
        }
    }

    override fun select(pageable: Pageable): UsersResultView {
        TODO("Not yet implemented")
    }

    override fun insert(user: UserInsertView): UserResultView {
        TODO("Not yet implemented")
    }

    override fun update(userId: Long, user: UserInsertView): UserResultView {
        TODO("Not yet implemented")
    }

    override fun delete(userId: Long): SimpleResultView<Boolean> {
        TODO("Not yet implemented")
    }
}
