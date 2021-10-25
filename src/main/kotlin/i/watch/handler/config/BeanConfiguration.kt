package i.watch.handler.config

import i.watch.handler.config.properties.SoftConfigProperties
import i.watch.utils.RedisUtils
import i.watch.utils.SnowFlakeUtils
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class BeanConfiguration {
    /**
     *  雪花 ID 生成器
     */
    @Bean
    fun idGenerate() = SnowFlakeUtils(0, 1L)

    @Bean
    fun redisUtils(
        redisTemplate: RedisTemplate<String, String>,
        softConfigProperties: SoftConfigProperties
    ): RedisUtils {
        return RedisUtils(redisTemplate, softConfigProperties.name)
    }
}
