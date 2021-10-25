package i.watch.handler.config

import i.watch.utils.RedisUtils
import i.watch.utils.SnowFlakeUtils
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class BeanConfiguration {
    @Bean
    fun idGenerate() = SnowFlakeUtils(0, 1L)

    @Bean
    fun redisUtils(redisTemplate: RedisTemplate<String, String>): RedisUtils {
        return RedisUtils(redisTemplate)
    }
}
