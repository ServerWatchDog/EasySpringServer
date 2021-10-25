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
    fun setInt(key: String, data: Int): Optional<Int>
    fun setLong(key: String, data: Long): Optional<Long>
    fun setString(key: String, data: Int): Optional<String>
    fun setBoolean(key: String, data: Int): Optional<Boolean>
}
