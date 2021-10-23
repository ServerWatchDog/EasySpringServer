package i.watch.config

import i.watch.utils.SnowFlake
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class IdGenerateConfiguration {
    @Bean
    fun idGenerate() = SnowFlake(0, 1L)
}
