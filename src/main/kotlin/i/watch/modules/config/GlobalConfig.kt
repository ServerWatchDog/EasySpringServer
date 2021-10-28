package i.watch.modules.config

import i.watch.handler.inject.config.ISoftConfig
import i.watch.handler.inject.config.SoftConfig
import i.watch.handler.inject.config.SoftConfigColumn
import i.watch.handler.inject.encrypt.EncryptView

@SoftConfig("global.config")
interface GlobalConfig : ISoftConfig {
    @SoftConfigColumn(key = "type", defaultEnv = "global.server.encrypt.type")
    var type: EncryptView.EncryptType

    @SoftConfigColumn(key = "publicKey", defaultEnv = "global.server.encrypt.public-key")
    var publicKey: String

    @SoftConfigColumn(key = "privateKey", defaultEnv = "global.server.encrypt.private-key")
    var privateKey: String

    @SoftConfigColumn(key = "installed", defaultValue = "false")
    var installed: Boolean
}
