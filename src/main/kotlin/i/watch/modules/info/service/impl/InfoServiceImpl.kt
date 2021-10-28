package i.watch.modules.info.service.impl

import com.github.open_edgn.security4k.asymmetric.rsa.RsaPrivate
import com.github.open_edgn.security4k.asymmetric.rsa.RsaPublic
import com.github.open_edgn.security4k.asymmetric.universal.IPrivateKey
import com.github.open_edgn.security4k.asymmetric.universal.IPublicKey
import i.watch.handler.inject.encrypt.EncryptView
import i.watch.modules.config.GlobalConfig
import i.watch.modules.info.model.view.EncryptResultView
import i.watch.modules.info.model.view.SoftwareInfoResultView
import i.watch.modules.info.service.IInfoService
import org.springframework.stereotype.Service

@Service
class InfoServiceImpl(
    private val globalConfig: GlobalConfig
) : IInfoService {
    override fun encryptInfo(): EncryptResultView {
        return EncryptResultView(globalConfig.type, globalConfig.publicKey)
    }

    override fun getPublicKey(type: EncryptView.EncryptType): IPublicKey {
        return when (type) {
            EncryptView.EncryptType.RSA -> {
                RsaPublic(globalConfig.publicKey)
            }
        }
    }

    override fun getPrivateKey(type: EncryptView.EncryptType): IPrivateKey {
        return when (type) {
            EncryptView.EncryptType.RSA -> {
                RsaPrivate(globalConfig.privateKey)
            }
        }
    }

    override fun getInfo(): SoftwareInfoResultView {
        return SoftwareInfoResultView(installed = globalConfig.installed, encrypt = encryptInfo())
    }
}
