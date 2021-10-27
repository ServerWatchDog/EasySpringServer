package i.watch.utils.cache.redis

import i.watch.utils.cache.KeyExistsException
import i.watch.utils.cache.LightDB
import i.watch.utils.cache.LightDBMap
import i.watch.utils.cache.StringParser
import org.springframework.data.redis.core.RedisTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional

class RedisLightDB(
    private val name: String = "",
    private val redisTemplate: RedisTemplate<String, String>,
    private val stringParser: StringParser,
) : LightDB {
    override fun getMap(key: String): Optional<LightDBMap> {
        return try {
            Optional.of(RedisLightDBMap(wrapKey(key), redisTemplate, stringParser))
        } catch (e: Exception) {
            Optional.empty()
        }
    }

    override fun createMap(key: String): LightDBMap {
        if (redisTemplate.opsForHash<String, String>().putIfAbsent(
                wrapKey(key), "_init",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            ).not()
        ) {
            throw KeyExistsException("KEY '$key' 已存在")
        }
        return getMap(key).get()
    }

    override fun createOrGetMap(key: String): LightDBMap {
        redisTemplate.opsForHash<String, String>().putIfAbsent(
            wrapKey(key), "_init",
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
        return getMap(key).get()
    }

    override fun drop(key: String): Boolean {
        return redisTemplate.delete(wrapKey(key))
    }

    private fun wrapKey(key: String): String {
        return if (name.isNotBlank()) {
            "$name:$key"
        } else {
            key
        }
    }

    override fun clearMap(key: String) {
        // TODO: 存在线程安全性问题
        drop(key)
        createMap(key)
    }
}
