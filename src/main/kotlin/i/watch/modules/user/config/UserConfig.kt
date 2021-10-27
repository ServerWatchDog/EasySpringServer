package i.watch.modules.user.config

import i.watch.handler.inject.config.ISoftConfig
import i.watch.handler.inject.config.SoftConfig
import i.watch.handler.inject.config.SoftConfigColumn

@SoftConfig("config.modules.user")
interface UserConfig : ISoftConfig {
    /**
     * 默认注册角色 ID
     */
    @SoftConfigColumn(key = "default-group-id", defaultValue = "-1")
    var defaultGroupId: Long

    /**
     * 允许注册
     */
    @SoftConfigColumn(key = "allow-register", defaultValue = "true")
    var allowRegister: Boolean
}
