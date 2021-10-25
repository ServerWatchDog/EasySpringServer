package i.watch.modules.info.service

import com.github.open_edgn.security4k.asymmetric.universal.IPrivateKey
import i.watch.handler.security.encrypt.EncryptView.EncryptType
import i.watch.modules.info.model.view.EncryptResultView

interface IInfoService {
    fun encryptInfo(): EncryptResultView
    fun getPrivateKey(type: EncryptType): IPrivateKey
}
