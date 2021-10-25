package i.watch.modules.config.hook

import i.watch.modules.config.service.IConfigBuildService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(0)
@Component
class ConfigStartHook(
    private val configService: IConfigBuildService
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        configService.updateConfig()
    }
}
