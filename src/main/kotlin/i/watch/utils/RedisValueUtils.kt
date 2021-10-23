package i.watch.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.Resource
import kotlin.reflect.KClass

@Component
final class RedisValueUtils {
    @Resource
    private lateinit var stringRedisTemplate: StringRedisTemplate

    @Resource(name = "redisJacksonManager")
    private lateinit var objectManager: ObjectMapper

    private val header = "note_"

    inline fun <reified T : Any> getValue(key: String): T {
        return getValue(key, T::class)
    }

    inline fun <reified T : Any> getValueOrElse(key: String, noinline elseFun: (String) -> T): T {
        return getValueOrElse(key, T::class, elseFun)
    }

    fun <T : Any> getValueOrElse(key: String, implClass: KClass<T>, elseFun: (String) -> T): T {
        val data = stringRedisTemplate.opsForValue().get(generateKey(key))
        return if (data != null) {
            objectManager.readValue(data, implClass.javaObjectType)
        } else {
            elseFun(key)
        }
    }

    fun <T : Any> getValue(key: String, implClass: KClass<T>): T {
        return getValueOrElse(key, implClass) {
            throw NullPointerException("未找到 $key 键.")
        }
    }

    fun <T : Any> putValue(key: String, data: T): T {
        val dataStr = objectManager.writeValueAsString(data)
        stringRedisTemplate.opsForValue().set(generateKey(key), dataStr)
        return data
    }

    fun <T : Any> putValue(key: String, data: T, timeout: Long, timeoutUnit: TimeUnit): T {
        val dataStr = objectManager.writeValueAsString(data)
        stringRedisTemplate.opsForValue().set(generateKey(key), dataStr, timeout, timeoutUnit)
        return data
    }

    fun putIfAbsent(key: String, data: Any): Boolean {
        val dataStr = objectManager.writeValueAsString(data)
        return stringRedisTemplate.opsForValue().setIfAbsent(generateKey(key), dataStr) ?: false
    }

    fun putIfAbsent(key: String, data: Any, timeout: Long, timeoutUnit: TimeUnit): Boolean {
        val dataStr = objectManager.writeValueAsString(data)
        return stringRedisTemplate.opsForValue().setIfAbsent(generateKey(key), dataStr, timeout, timeoutUnit) ?: false
    }

    fun expire(key: String, timeout: Long, timeoutUnit: TimeUnit): Boolean {
        return stringRedisTemplate.boundValueOps(generateKey(key)).expire(timeout, timeoutUnit) ?: false
    }

    private fun generateKey(key: String) = "$header$key"
    fun deleteById(key: String): Boolean {
        return stringRedisTemplate.delete(generateKey(key))
    }

    fun getExpire(key: String): Long {
        return stringRedisTemplate.boundValueOps(generateKey(key)).expire ?: -1
    }

    fun keyExistsOrElse(key: String, function: () -> Boolean): Boolean {
        stringRedisTemplate.opsForValue().get(generateKey(key)) ?: return function()
        return true
    }
}
