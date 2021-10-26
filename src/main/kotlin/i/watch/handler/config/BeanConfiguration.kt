package i.watch.handler.config

import com.fasterxml.jackson.databind.ObjectMapper
import i.watch.handler.config.properties.SoftConfigProperties
import i.watch.utils.HashUtils
import i.watch.utils.SnowFlakeUtils
import i.watch.utils.cache.JacksonStringParser
import i.watch.utils.cache.LightDB
import i.watch.utils.cache.redis.RedisLightDB
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
class BeanConfiguration {
    @Resource(name = "redisJacksonManager")
    private lateinit var objectManager: ObjectMapper

    /**
     *  雪花 ID 生成器
     */
    @Bean
    fun idGenerate() = SnowFlakeUtils(0, 1L)

    @Bean
    fun lightDB(
        redisTemplate: RedisTemplate<String, String>,
        softConfigProperties: SoftConfigProperties
    ): LightDB {
        return RedisLightDB(
            name = softConfigProperties.name,
            redisTemplate = redisTemplate, stringParser = JacksonStringParser(objectManager)
        )
    }

    @Bean
    fun hashUtils(
        softConfigProperties: SoftConfigProperties
    ) = HashUtils(saltCode = softConfigProperties.name)
}
