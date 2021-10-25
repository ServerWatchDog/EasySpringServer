package i.watch.modules.global.service.impl

import com.github.open_edgn.security4k.asymmetric.rsa.RsaPrivate
import com.github.open_edgn.security4k.asymmetric.universal.IPrivateKey
import i.watch.config.properties.EncryptProperties
import i.watch.config.properties.SoftConfigProperties
import i.watch.hooks.security.dec.EncryptView
import i.watch.modules.global.model.view.SoftEncryptView
import i.watch.modules.global.service.ISoftInfoService
import org.springframework.stereotype.Service

@Service
class SoftInfoServiceImpl(
    private val softConfig: SoftConfigProperties,
    private val encryptConfig: EncryptProperties
) : ISoftInfoService {
    override fun encryptInfo(): SoftEncryptView {
        return SoftEncryptView(encryptConfig.type, encryptConfig.publicKey)
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
