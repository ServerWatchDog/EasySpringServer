package i.watch.modules.installer.controller

import i.watch.modules.installer.model.InstallInitResultView
import i.watch.modules.installer.model.view.InstallInitView
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/installer")
class InstallController {
    @PostMapping("/init")
    fun install(init: InstallInitView): InstallInitResultView {
        TODO()
    }
}
