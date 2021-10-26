package i.watch.utils.cache.redis

import i.watch.utils.cache.LightDBMap
import i.watch.utils.cache.StringParser
import i.watch.utils.getLogger
import org.springframework.data.redis.core.RedisTemplate
import java.util.Optional
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class RedisLightDBMap(
    private val superKey: String,
    private val redisTemplate: RedisTemplate<String, String>,
    private val parser: StringParser
) : LightDBMap {
    companion object {
        private val logger = getLogger()
    }

    init {
        checkExists {}
    }

    override val expire: Long
        get() = checkExists {
            redisTemplate.boundHashOps<String, String>(superKey).expire ?: -1
        }

    override fun <T : Any> get(key: String, format: (String) -> T?): Optional<T> {
        return checkExists {
            val data = it.opsForHash<String, String>().get(superKey, key)
                ?: return@checkExists Optional.empty<T>()
            Optional.ofNullable(format(data))
        }
    }

    override fun <T : Any> get(key: String, clazz: KClass<T>): Optional<T> {
        return get(key) {
            parser.fromString(it, clazz)
        }
    }

    override fun <T : Any> putValue(key: String, data: T): T {
        return checkExists {
            it.opsForHash<String, String>()
                .put(superKey, key, parser.toString(data))
            data
        }
    }

    override fun delete(key: String): Boolean {
        return checkExists {
            it.opsForHash<String, String>().delete(superKey, key) != 0L
        }
    }

    override fun <T : Any> putIfAbsent(key: String, data: T): T {
        return checkExists {
            if (it.opsForHash<String, String>()
                .putIfAbsent(superKey, key, parser.toString(data))
            ) {
                data
            } else {
                get(key, data::class).get()
            }
        }
    }

    override fun expire(timeout: Long, unit: TimeUnit): Boolean {
        return checkExists {
            redisTemplate.boundHashOps<String, String>(superKey).expire(timeout, unit) ?: false
        }
    }

    private fun <T : Any> checkExists(function: (RedisTemplate<String, String>) -> T): T {
        return if (redisTemplate.opsForHash<String, String>().hasKey(superKey, "_init")) {
            function(redisTemplate)
        } else {
            logger.debug("名为 '{}' 的RedisMap数据不存在.", superKey)
            throw NullPointerException("此值不存在.")
        }
    }
}
