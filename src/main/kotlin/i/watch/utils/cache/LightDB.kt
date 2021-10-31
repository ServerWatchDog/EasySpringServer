package i.watch.utils.cache

import java.util.Optional

/**
 * 轻量级数据存储器
 */
interface LightDB {
    /**
     * 获取 MAP 实例，如果不存在则返回 EMPTY
     */
    fun getMap(key: String): Optional<LightDBMap>

    /**
     *  创建 MAP 实例，如果已存在则抛出异常
     */
    fun createMap(key: String): LightDBMap

    /**
     * 创建 MAP 实例，如果已存在则直接获取
     */
    fun getOrCreateMap(key: String, create: (LightDBMap) -> Unit = {}): LightDBMap

    /**
     *  删除MAP 实例
     */
    fun drop(key: String): Boolean

    /**
     * 重新初始化 Map
     */
    fun clearMap(key: String)
}
