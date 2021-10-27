package i.watch.modules.info.service

import com.github.open_edgn.security4k.asymmetric.universal.IPrivateKey
import com.github.open_edgn.security4k.asymmetric.universal.IPublicKey
import i.watch.handler.security.encrypt.EncryptView.EncryptType
import i.watch.modules.info.model.view.EncryptResultView
import i.watch.modules.info.model.view.SoftwareInfoResultView

interface IInfoService {
    fun encryptInfo(): EncryptResultView
    fun getPrivateKey(type: EncryptType): IPrivateKey
    fun getPublicKey(type: EncryptType): IPublicKey
    fun getInfo(): SoftwareInfoResultView
}
