package i.watch.modules.global.model.view

import i.watch.hooks.security.dec.EncryptView

data class SoftEncryptView(
    val type: EncryptView.EncryptType,
    val publicKey: String
)
