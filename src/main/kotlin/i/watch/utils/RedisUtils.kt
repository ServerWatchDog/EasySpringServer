package i.watch.utils

import com.fasterxml.jackson.databind.ObjectMapper
import i.watch.handler.security.session.ISession
import org.springframework.data.redis.core.RedisTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional
import java.util.concurrent.TimeUnit
import javax.annotation.Resource
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class RedisUtils(
    private val rawRedisTemplate: RedisTemplate<String, String>
) {
    @Resource(name = "redisJacksonManager")
    private lateinit var objectManager: ObjectMapper

    fun withMap(key: String): RedisMapUtils {
        return RedisMapUtils(rawRedisTemplate, objectManager, key)
    }

    fun dropMap(key: String) {
        rawRedisTemplate.delete(key)
    }

    fun initMap(key: String): RedisMapUtils {
        if (rawRedisTemplate.opsForHash<String, String>().putIfAbsent(
                key, "_init",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            ).not()
        ) {
            throw KeyExistsException("KEY '$key' 已存在")
        }
        return withMap(key)
    }

    class KeyExistsException(msg: String) : RuntimeException(msg)
    class RedisMapUtils(
        private val redisTemplate: RedisTemplate<String, String>,
        private val objectManager: ObjectMapper,
        private val mapKey: String
    ) {
        companion object {
            private val logger = getLogger()
        }

        init {
            checkExists {}
        }

        /**
         * 过期时间
         */
        val expire: Long
            get() = checkExists {
                redisTemplate.boundHashOps<String, String>(mapKey).expire ?: -1
            }

        private fun <T : Any> checkExists(function: (RedisTemplate<String, String>) -> T): T {
            return if (redisTemplate.opsForHash<String, String>().hasKey(mapKey, "_init")) {
                function(redisTemplate)
            } else {
                logger.debug("名为 '{}' 的RedisMap数据不存在.", mapKey)
                throw NullPointerException("此值不存在.")
            }
        }

        /**
         * 获取对象
         */
        inline fun <reified T : Any> get(key: String): Optional<T> {
            return get(key, T::class)
        }

        fun <T : Any> get(key: String, format: (String) -> T?): Optional<T> {
            return checkExists {
                val data = it.opsForHash<String, String>().get(mapKey, key)
                    ?: return@checkExists Optional.empty<T>()
                Optional.ofNullable(format(data))
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> get(key: String, clazz: KClass<T>): Optional<T> {
            return get(key) {
                if (clazz.isSubclassOf(CharSequence::class)) {
                    it as T
                } else {
                    objectManager.readValue(
                        it,
                        clazz.javaObjectType
                    )
                }
            }
        }

        fun <T : Any> putValue(key: String, data: T): T {
            return checkExists {
                val func: () -> String =
                    if (data::class.isSubclassOf(CharSequence::class)) {
                        {
                            data.toString()
                        }
                    } else {
                        {
                            objectManager.writeValueAsString(data)
                        }
                    }
                it.opsForHash<String, String>()
                    .put(mapKey, key, func())

                data
            }
        }

        fun delete(key: String): Boolean {
            return checkExists {
                it.opsForHash<String, String>().delete(mapKey, key) != 0L
            }
        }

        fun <T : Any> putIfAbsent(key: String, data: T): T {
            return checkExists {
                val func: () -> String =
                    if (data::class.isSubclassOf(CharSequence::class)) {
                        {
                            data.toString()
                        }
                    } else {
                        {
                            objectManager.writeValueAsString(data)
                        }
                    }
                if (it.opsForHash<String, String>()
                    .putIfAbsent(mapKey, key, func())
                ) {
                    data
                } else {
                    get(key, data::class).get()
                }
            }
        }

        /**
         * 设置密钥的生存时间/到期时间。
         *
         * @param timeout 过期时间
         * @param unit 时间单位
         * @return 如果设置了过期时间，则为 true，否则为 false。  在管道事务中使用时返回 false。
         */
        fun expire(timeout: Long, unit: TimeUnit): Boolean {
            return checkExists {
                redisTemplate.boundHashOps<String, String>(mapKey).expire(timeout, unit) ?: false
            }
        }
    }

    abstract class RedisSession(
        protected val redisMapUtils: RedisMapUtils,
        private val timeOut: Long,
        private val timeUnit: TimeUnit
    ) : ISession {
        val createTime: LocalDateTime
            get() = LocalDateTime.from(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(
                    redisMapUtils.get<String>("_init").get()
                )
            )

        override fun refresh() {
            redisMapUtils.expire(timeOut, timeUnit)
            redisMapUtils.putValue("_update", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        }
    }
}
