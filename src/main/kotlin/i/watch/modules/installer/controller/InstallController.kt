package i.watch.modules.installer.controller

import i.watch.hooks.security.dec.CryptRequestBody
import i.watch.modules.installer.model.tag.InstallPermission
import i.watch.modules.installer.model.view.InstallInitResultView
import i.watch.modules.installer.model.view.InstallInitView
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${global.server.api}/install")
class InstallController {
    //    @Permission("inst")
    @InstallPermission
    @PostMapping("/init")
    fun install(
        @RequestBody init: InstallInitView,
        @CryptRequestBody data: Any
    ): InstallInitResultView {
        println(init)
        return InstallInitResultView("asas")
    }
}
