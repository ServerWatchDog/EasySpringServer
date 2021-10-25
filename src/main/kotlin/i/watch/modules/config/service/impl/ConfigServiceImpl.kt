package i.watch.modules.config.service.impl

import i.watch.handler.config.properties.SoftConfigProperties
import i.watch.modules.config.model.db.ConfigEntity
import i.watch.modules.config.repository.ConfigRepository
import i.watch.modules.config.service.IConfigService
import i.watch.utils.RedisUtils
import org.springframework.stereotype.Service
import java.util.Optional

/**
 * 带缓存的配置文件加载器
 */
@Service
class ConfigServiceImpl(
    redisUtils: RedisUtils,
    config: SoftConfigProperties,
    private val configRepository: ConfigRepository
) : IConfigService {

    private var configContainer: RedisUtils.RedisMapUtils

    init {
        val key = "${config.name}::config"
        redisUtils.dropMap(key)
        this.configContainer = redisUtils.initMap(key)
    }

    override fun getInt(key: String): Optional<Int> {
        return configContainer.get<String>("int::$key").or {
            configRepository.getByKey(key).map { it.value }
        }.map { it.toInt() }
    }

    override fun getLong(key: String): Optional<Long> {
        return configContainer.get<String>("long::$key").or {
            configRepository.getByKey(key).map { it.value }
        }.map { it.toLong() }
    }

    override fun getString(key: String): Optional<String> {
        return configContainer.get<String>("string::$key").or {
            configRepository.getByKey(key).map { it.value }
        }
    }

    override fun getBoolean(key: String): Optional<Boolean> {
        return configContainer.get<String>("boolean::$key").or {
            configRepository.getByKey(key).map { it.value }
        }.map { it.toBoolean() }
    }

    override fun setInt(key: String, data: Int): Optional<Int> {
        val old = getInt(key)
        configContainer.delete("int::$key")
        configRepository.saveAndFlush(ConfigEntity(key, data.toString()))
        return old
    }

    override fun setLong(key: String, data: Long): Optional<Long> {
        val old = getLong(key)
        configContainer.delete("long::$key")
        configRepository.saveAndFlush(ConfigEntity(key, data.toString()))
        return old
    }

    override fun setString(key: String, data: Int): Optional<String> {
        val old = getString(key)
        configContainer.delete("string::$key")
        configRepository.saveAndFlush(ConfigEntity(key, data.toString()))
        return old
    }

    override fun setBoolean(key: String, data: Int): Optional<Boolean> {
        val old = getBoolean(key)
        configContainer.delete("boolean::$key")
        configRepository.saveAndFlush(ConfigEntity(key, data.toString()))
        return old
    }
}
