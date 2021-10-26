package i.watch.modules.user.config

import i.watch.handler.inject.config.ISoftConfig
import i.watch.handler.inject.config.SoftConfig

@SoftConfig("config.modules.user")
interface UserConfig : ISoftConfig {
    var defaultUserId: String
}
