package i.watch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.web.servlet.config.annotation.EnableWebMvc


@EnableCaching
@EnableWebMvc
@SpringBootApplication
@EnableSpringDataWebSupport
@ConfigurationPropertiesScan("i.watch")
class WatchApplication

fun main(args: Array<String>) {
    runApplication<WatchApplication>(*args)
}
