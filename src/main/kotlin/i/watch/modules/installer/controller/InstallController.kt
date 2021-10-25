package i.watch.modules.installer.controller

import i.watch.hooks.security.dec.CryptRequestBody
import i.watch.modules.installer.model.tag.InstallPermission
import i.watch.modules.installer.model.view.InstallInitView
import i.watch.modules.installer.service.InstallService
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
