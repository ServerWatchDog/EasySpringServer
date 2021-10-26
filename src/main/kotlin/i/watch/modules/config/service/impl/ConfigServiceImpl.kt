package i.watch.modules.config.service.impl

import i.watch.modules.config.model.db.ConfigEntity
import i.watch.modules.config.repository.ConfigRepository
import i.watch.modules.config.service.IConfigService
import i.watch.utils.cache.LightDB
import i.watch.utils.cache.LightDBMap
import i.watch.utils.cache.get
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 带缓存的配置文件加载器
 */
@Service
class ConfigServiceImpl(
    lightDB: LightDB,
    private val configRepository: ConfigRepository
) : IConfigService {

    private var configContainer: LightDBMap

    init {
        val key = "config"
        lightDB.drop(key)
        this.configContainer = lightDB.createMap(key)
    }

    override fun getString(key: String): Optional<String> {
        return configContainer.get<String>(key).or {
            configRepository.getByKey(key).map { configContainer.putValue(key, it.value) }
        }
    }

    override fun getCachedString(key: String, cache: String): Optional<String> {
        return configContainer.get<String>(key).or {
            configRepository.getByKey(key)
                .map { configContainer.putValue(key, it.value) }.or {
                    Optional.of(configContainer.putValue(key, cache))
                }
        }
    }

    override fun setString(key: String, data: String): Optional<String> {
        val old = getString(key)
        configContainer.delete(key)
        configRepository.saveAndFlush(ConfigEntity(key, data))
        return old
    }

    override fun string(key: String, defaultValue: String) = object : ReadWriteProperty<Any?, String> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return getString(key).orElse(defaultValue)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            setString(key, value)
        }
    }
}
