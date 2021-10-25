package i.watch.modules.installer.hooks

import i.watch.modules.global.repository.ConfigRepository
import i.watch.modules.installer.service.InstallSessionService
import i.watch.utils.RedisUtils
import i.watch.utils.getLogger
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class InstallStartHook(
    private val configRepository: ConfigRepository,
    private val redisUtils: RedisUtils,
    private val installSessionService: InstallSessionService
) : ApplicationRunner {
    val installed: Boolean by lazy {
        configRepository.getByKey(INSTALL_FINISH_KEY).isPresent
    }

    override fun run(args: ApplicationArguments?) {
        configRepository.getByKey(INSTALL_FINISH_KEY)
            .ifPresentOrElse(
                {
                    logger.info("此项目安装于 {}.", it)
                }
            ) {
                logger.error("项目未安装.")
                installSessionService.initToken()
            }
    }

    companion object {
        private val logger = getLogger()
        const val INSTALL_FINISH_KEY = "install.finish"
    }
}
