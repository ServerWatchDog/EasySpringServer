package i.watch.modules.install.service.impl

import i.watch.handler.security.session.ISessionService.GenericISessionService
import i.watch.modules.config.service.IConfigService
import i.watch.modules.install.model.view.InstallInitResultView
import i.watch.modules.install.model.view.InstallInitView
import i.watch.modules.install.service.InstallService
import i.watch.utils.RedisUtils
import i.watch.utils.TokenUtils
import i.watch.utils.getLogger
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Service("AUTH:inst")
class InstallServiceImpl(
    private val redisUtils: RedisUtils,
    private val configService: IConfigService
) : GenericISessionService<InstallerSession>, InstallService {

    override val installed: Boolean by lazy {
        configService.getString(INSTALL_FINISH_KEY).isPresent
    }

    override fun install(init: InstallInitView): InstallInitResultView {
        TODO("Not yet implemented")
    }

    override fun checkInstall() {
        if (!installed) {
            logger.error("项目未安装.")
            val token = TokenUtils.randomToken("inst")
            redisUtils.initMap(key = "session:installer:$token").expire(1, TimeUnit.DAYS)
            println(
                """
  ==============================================================
                           安 装
  安装密钥: $token
  
  注意，此密钥仅在 1 天内 (${
                LocalDateTime.now().plusDays(1)
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                } 之前) 有效
  
  ==============================================================
                """.trimIndent()
            )
        }
    }

    override fun getSession(token: String): InstallerSession {
        return InstallerSession(redisUtils.withMap("session:installer:$token"))
    }

    override fun verifySession(session: InstallerSession, permissions: Array<String>): Boolean {

        return true
    }

    companion object {
        private val logger = getLogger()
        const val INSTALL_FINISH_KEY = "install.finish"
    }
}
