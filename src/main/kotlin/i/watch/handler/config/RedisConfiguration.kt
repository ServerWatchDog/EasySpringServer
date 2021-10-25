package i.watch.handler.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Redis 转义
 */
@Configuration
class RedisConfiguration {
    @Bean(name = ["redisJacksonManager"])
    fun redisJacksonManager(): ObjectMapper {
        val om = ObjectMapper()
        om.registerModule(KotlinModule())
        om.registerModule(JavaTimeModule())
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        om.activateDefaultTyping(LaissezFaireSubTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL)
        return om
    }
}
