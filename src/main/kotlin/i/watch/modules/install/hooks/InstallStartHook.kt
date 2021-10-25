package i.watch.modules.install.hooks

import i.watch.modules.install.service.InstallService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class InstallStartHook(
    private val installService: InstallService

) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        installService.checkInstall()
    }
}
