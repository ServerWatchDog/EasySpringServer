package i.watch.modules.config.service

import java.util.Optional
import kotlin.properties.ReadWriteProperty

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
    fun setString(key: String, data: String): Optional<String>
    fun setBoolean(key: String, data: Boolean): Optional<Boolean>
    fun int(key: String, defaultValue: Int): ReadWriteProperty<Any?, Int>
    fun long(key: String, defaultValue: Long): ReadWriteProperty<Any?, Long>
    fun string(key: String, defaultValue: String): ReadWriteProperty<Any?, String>
    fun boolean(key: String, defaultValue: Boolean): ReadWriteProperty<Any?, Boolean>
}
