package i.watch.modules.info.service.impl

import com.github.open_edgn.security4k.asymmetric.rsa.RsaPrivate
import com.github.open_edgn.security4k.asymmetric.universal.IPrivateKey
import i.watch.handler.config.properties.EncryptProperties
import i.watch.handler.config.properties.SoftConfigProperties
import i.watch.handler.security.encrypt.EncryptView
import i.watch.modules.config.service.IConfigService
import i.watch.modules.info.service.IInfoService
import org.springframework.stereotype.Service

@Service
class InfoServiceImpl(
    private val softConfig: SoftConfigProperties,
    private val configService: IConfigService,
    private val encryptConfig: EncryptProperties,
) : IInfoService {
    override fun encryptInfo(): i.watch.modules.info.model.view.EncryptResultView {
        return i.watch.modules.info.model.view.EncryptResultView(encryptConfig.type, encryptConfig.publicKey)
    }

    @Suppress("REDUNDANT_ELSE_IN_WHEN")
    override fun getPrivateKey(type: EncryptView.EncryptType): IPrivateKey {
        return when (type) {
            EncryptView.EncryptType.RSA -> {
                RsaPrivate(encryptConfig.privateKey)
            }
            // 扩展加密算法用
            else -> throw RuntimeException("不支持的解密算法.")
        }
    }
}
