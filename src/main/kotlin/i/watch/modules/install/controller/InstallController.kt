package i.watch.modules.install.controller

import i.watch.handler.security.encrypt.CryptRequestBody
import i.watch.modules.install.model.tag.InstallPermission
import i.watch.modules.install.model.view.InstallInitView
import i.watch.modules.install.service.InstallService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${global.server.api}/install")
class InstallController(
    private val installService: InstallService
) {
    @InstallPermission
    @PostMapping("/init")
    fun install(
        @CryptRequestBody init: InstallInitView
    ) = installService.install(init)
}
