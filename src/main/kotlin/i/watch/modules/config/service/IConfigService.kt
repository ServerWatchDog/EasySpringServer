package i.watch.modules.config.service

import java.util.Optional

/**
 * 配置文件载入
 */
interface IConfigService {
    fun getInt(key: String): Optional<Int>
    fun getLong(key: String): Optional<Long>
    fun getString(key: String): Optional<String>
    fun getBoolean(key: String): Optional<Boolean>
}
