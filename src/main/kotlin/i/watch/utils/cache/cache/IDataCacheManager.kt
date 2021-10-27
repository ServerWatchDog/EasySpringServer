package i.watch.utils.cache.cache

import kotlin.reflect.KClass

/**
 * 对象缓存
 */
interface IDataCacheManager {
    /**
     * 创建缓存实例
     */
    fun newCache(name: String): IDataCache

    /**
     * 清空缓存消息
     */
    fun clearAll(name: String)

    interface IDataCache {
        /**
         * 计算结果并缓存
         */
        fun <T : Any> cache(variable: String, returnType: KClass<T>, calc: () -> T): T

        /**
         * 清除缓存
         */
        fun clear(variable: String)
    }
}
