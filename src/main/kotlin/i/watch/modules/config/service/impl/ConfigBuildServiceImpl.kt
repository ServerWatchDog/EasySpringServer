package i.watch.modules.config.service.impl

import i.watch.handler.config.properties.EncryptProperties
import i.watch.handler.security.encrypt.EncryptView
import i.watch.modules.config.service.IConfigBuildService
import i.watch.modules.config.service.IConfigService
import i.watch.utils.getLogger
import org.springframework.stereotype.Service

@Service
class ConfigBuildServiceImpl(
    private val configService: IConfigService,
    private val encryptConfig: EncryptProperties
) : IConfigBuildService {
    override fun updateConfig() {
        updateEncrypt()
    }

    /**
     * 载入密钥信息
     */
    private fun updateEncrypt() {
        configService.getString("global.security.type").ifPresent {
            encryptConfig.type = EncryptView.EncryptType.valueOf(it)
            encryptConfig.publicKey = configService.getString("global.security.publicKey").get()
            encryptConfig.privateKey = configService.getString("global.security.privateKey").get()
            logger.debug("使用数据库配置替换默认公、私钥")
        }
    }

    companion object {
        private val logger = getLogger()
    }
}
