package i.watch.modules.info.service.impl

import com.github.open_edgn.security4k.asymmetric.rsa.RsaPrivate
import com.github.open_edgn.security4k.asymmetric.rsa.RsaPublic
import com.github.open_edgn.security4k.asymmetric.universal.IPrivateKey
import com.github.open_edgn.security4k.asymmetric.universal.IPublicKey
import i.watch.handler.config.properties.EncryptProperties
import i.watch.handler.config.properties.SoftConfigProperties
import i.watch.handler.security.encrypt.EncryptView
import i.watch.modules.config.service.IConfigService
import i.watch.modules.info.model.view.EncryptResultView
import i.watch.modules.info.service.IInfoService
import org.springframework.stereotype.Service

@Service
class InfoServiceImpl(
    private val softConfig: SoftConfigProperties,
    private val configService: IConfigService,
    private val encryptConfig: EncryptProperties,
) : IInfoService {
    override fun encryptInfo(): EncryptResultView {
        return EncryptResultView(encryptConfig.type, encryptConfig.publicKey)
    }

    override fun getPublicKey(type: EncryptView.EncryptType): IPublicKey {
        return when (type) {
            EncryptView.EncryptType.RSA -> {
                RsaPublic(encryptConfig.publicKey)
            }
        }
    }

    override fun getPrivateKey(type: EncryptView.EncryptType): IPrivateKey {
        return when (type) {
            EncryptView.EncryptType.RSA -> {
                RsaPrivate(encryptConfig.privateKey)
            }
        }
    }
}
