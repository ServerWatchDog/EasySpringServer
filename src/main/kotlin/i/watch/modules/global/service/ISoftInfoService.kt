package i.watch.modules.global.service

import com.github.open_edgn.security4k.asymmetric.universal.IPrivateKey
import i.watch.hooks.security.dec.EncryptView
import i.watch.modules.global.model.view.SoftEncryptView

interface ISoftInfoService {
    fun encryptInfo(): SoftEncryptView
    fun getPrivateKey(type: EncryptView.EncryptType): IPrivateKey
}
