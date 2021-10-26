package i.watch.modules.config.config

import i.watch.handler.inject.config.ISoftConfig
import i.watch.handler.inject.config.SoftConfig
import i.watch.handler.inject.config.SoftConfigColumn
import i.watch.handler.security.encrypt.EncryptView

@SoftConfig("global.config")
interface GlobalConfig : ISoftConfig {
    @SoftConfigColumn(key = "type", defaultEnv = "global.server.encrypt.type")
    val type: EncryptView.EncryptType

    @SoftConfigColumn(key = "publicKey", defaultEnv = "global.server.encrypt.public-key")
    val publicKey: String

    @SoftConfigColumn(key = "privateKey", defaultEnv = "global.server.encrypt.private-key")
    val privateKey: String
}
