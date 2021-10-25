package i.watch.modules.info.model.view

import i.watch.handler.security.encrypt.EncryptView

data class EncryptResultView(
    val type: EncryptView.EncryptType,
    val publicKey: String
)
