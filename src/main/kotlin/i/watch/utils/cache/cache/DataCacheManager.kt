package i.watch.utils.cache.cache

import i.watch.utils.cache.LightDB
import i.watch.utils.cache.LightDBMap
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 * 对象缓存
 * @property lightDB LightDB
 * @property keys MutableMap<String, DataCache<out Any>>
 * @constructor
 */
class DataCacheManager(
    private val lightDB: LightDB
) : IDataCacheManager {

    private val keys: MutableMap<String, DataCache> = ConcurrentHashMap()

    override fun newCache(name: String): DataCache {
        val key = "cache:$name"
        return keys.getOrPut(key) {
            DataCache(lightDB, key)
        }
    }

    override fun clearAll(name: String) {
        keys["cache:$name"]?.clear()
    }

    /**
     * 对象缓存
     */
    class DataCache(
        private val lightDB: LightDB,
        private val dbKey: String,
    ) : IDataCacheManager.IDataCache {
        @Volatile
        private var dbMap: LightDBMap

        init {
            lightDB.drop(dbKey)
            dbMap = lightDB.createMap(dbKey)
        }

        override fun <T : Any> cache(variable: String, returnType: KClass<T>, calc: () -> T): T {
            return dbMap.get(variable, returnType).or {
                Optional.of(dbMap.putIfAbsent(variable, calc()))
            }.get()
        }

        override fun clear(variable: String) {
            dbMap.delete(variable)
        }

        fun clear() {
            lightDB.clearMap(dbKey)
        }
    }
}
