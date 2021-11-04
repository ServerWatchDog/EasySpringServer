package i.watch.modules.user.service.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import i.watch.handler.advice.BadRequestException
import i.watch.handler.advice.ForbiddenException
import i.watch.modules.user.UserConfig
import i.watch.modules.user.model.db.QAuthorityEntity
import i.watch.modules.user.model.db.QGroupEntity
import i.watch.modules.user.model.db.QUserEntity
import i.watch.modules.user.model.db.UserEntity
import i.watch.modules.user.model.view.login.LoginResultView
import i.watch.modules.user.model.view.login.LoginView
import i.watch.modules.user.model.view.register.RegisterResultView
import i.watch.modules.user.model.view.register.RegisterView
import i.watch.modules.user.model.view.user.UserInfoResultView
import i.watch.modules.user.model.view.user.UserInsertView
import i.watch.modules.user.model.view.user.UserResultView
import i.watch.modules.user.repository.UserRepository
import i.watch.modules.user.service.IUserService
import i.watch.modules.user.service.IUserSessionService
import i.watch.utils.DateTimeUtils
import i.watch.utils.EmailImageUtils
import i.watch.utils.HashUtils
import i.watch.utils.SnowFlakeUtils
import i.watch.utils.cache.cache.IDataCacheManager
import i.watch.utils.cache.cache.cache
import i.watch.utils.getLogger
import i.watch.utils.template.crud.CRUDServiceImpl
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import javax.annotation.Resource
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil.generateCurrentNumberString as twoFactoryGem

@Service
class UserServiceImpl(
    private val jpaQuery: JPAQueryFactory,
    override val repository: UserRepository,
    private val hashUtils: HashUtils,
    private val userConfig: UserConfig,
    private val idGenerator: SnowFlakeUtils,
    dataCacheManager: IDataCacheManager
) : IUserService, CRUDServiceImpl<UserInsertView, UserResultView, Long, UserEntity>() {

    private val authorityCache = dataCacheManager.newCache("authority")

    @Resource
    private lateinit var userSessionService: IUserSessionService

    private val logger = getLogger()
    override fun tryLogin(loginView: LoginView): LoginResultView {
        val qUser = QUserEntity.userEntity
        return jpaQuery.selectFrom(qUser)
            .where(qUser.email.eq(loginView.account.lowercase()))
            .fetchOne()
            .let { Optional.ofNullable(it) }
            .filter {
                hashUtils.verify(loginView.password, it.password)
            }.or {
                logger.debug("未找到邮箱为 {} 的用户.", loginView.account)
                throw BadRequestException("用户名或密码错误")
            }.filter {
                it.twoFactor.isEmpty() ||
                    twoFactoryGem(it.twoFactor) == loginView.code
            }.map {
                LoginResultView(
                    // 登录成功
                    LoginResultView.LoginResultType.SUCCESS,
                    userSessionService.createSessionByUserId(it.id)
                )
            }.or {
                logger.debug("用户  {} 二步验证未通过.", loginView.account)
                Optional.of(LoginResultView(LoginResultView.LoginResultType.NEED_2FA, ""))
            }.orElseThrow {
                BadRequestException("用户名或密码错误")
            }
    }

    override fun tryRegister(user: RegisterView): RegisterResultView {
        if (!userConfig.allowRegister) {
            throw BadRequestException("管理员配置不允许注册.")
        }
        val group = jpaQuery
            .select(QGroupEntity.groupEntity.name)
            .from(QGroupEntity.groupEntity)
            .where(QGroupEntity.groupEntity.id.eq(userConfig.defaultGroupId))
            .fetchOne() ?: throw BadRequestException("管理员配置不允许注册.")
        insert(
            input = UserInsertView(
                name = user.name,
                email = user.email,
                password = user.password,
                group = group
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

    override fun tableToOutput(table: UserEntity): UserResultView {
        return table.let {
            UserResultView(
                id = it.id.toString(),
                name = it.name,
                email = it.email,
                password = "******",
                registerDate = DateTimeUtils.formatDateTime(it.registerTime),
                groupName = it.linkGroup.name
            )
        }
    }

    override fun userInfo(userSession: UserSessionServiceImpl.UserSession): UserInfoResultView {
        val qUser = QUserEntity.userEntity
        val select = jpaQuery.select(qUser.name, qUser.email, qUser.linkGroup.name)
            .from(qUser)
            .where(qUser.id.eq(userSession.userId)).fetchOne() ?: throw RuntimeException("未知错误！")
        return UserInfoResultView(
            name = select.get(qUser.name)!!,
            url = EmailImageUtils.getImageUrl(select.get(qUser.email)!!),
            groupName = select.get(qUser.linkGroup.name)!!
        )
    }

    override fun inputToTable(table: Optional<UserEntity>, input: UserInsertView): UserEntity {
        val defaultGroup = jpaQuery.selectFrom(QGroupEntity.groupEntity)
            .where(QGroupEntity.groupEntity.name.eq(input.group)).fetchOne()
            ?: throw BadRequestException("未找到名为 ${input.group} 的组.")
        return if (table.isEmpty) {
            if (repository.existsByEmail(input.email.lowercase())) {
                throw ForbiddenException("邮箱已被注册.")
            }
            UserEntity(
                id = idGenerator.nextId(),
                email = input.email.lowercase(),
                name = input.name,
                password = hashUtils.create(input.password),
                twoFactor = "",
                registerTime = LocalDateTime.now(),
                linkGroup = defaultGroup
            )
        } else {
            table.get().apply {
                email = input.email.lowercase()
                name = input.name
                password = hashUtils.create(input.password)
                twoFactor = ""
                linkGroup = defaultGroup
            }
        }
    }

    override fun afterWriteHook(table: UserEntity) {
        authorityCache.clear(table.id.toString()) // 进行用户操作后清除作业缓存
    }
}
