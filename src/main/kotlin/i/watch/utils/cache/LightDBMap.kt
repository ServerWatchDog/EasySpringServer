package i.watch.utils.cache

import java.util.Optional
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

interface LightDBMap {
    val expire: Long
    fun <T : Any> get(key: String, format: (String) -> T?): Optional<T>
    fun <T : Any> get(key: String, clazz: KClass<T>): Optional<T>
    fun <T : Any> putValue(key: String, data: T): T
    fun delete(key: String): Boolean
    fun <T : Any> putIfAbsent(key: String, data: T): T
    fun expire(timeout: Long, unit: TimeUnit): Boolean
}
