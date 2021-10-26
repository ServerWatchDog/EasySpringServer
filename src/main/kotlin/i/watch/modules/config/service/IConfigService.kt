package i.watch.modules.config.service

import java.util.Optional
import kotlin.properties.ReadWriteProperty

/**
 * 配置文件载入
 */
interface IConfigService {
    fun getString(key: String): Optional<String>

    /**
     * 缓解缓存穿透问题
     */
    fun getCachedString(key: String, cache: String): Optional<String>
    fun setString(key: String, data: String): Optional<String>
    fun string(key: String, defaultValue: String): ReadWriteProperty<Any?, String>
}
