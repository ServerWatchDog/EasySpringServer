package i.watch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableWebMvc
@SpringBootApplication
@EnableSpringDataWebSupport
@ConfigurationPropertiesScan("i.watch")
class WatchApplication

fun main(args: Array<String>) {
    runApplication<WatchApplication>(*args)
}
    
