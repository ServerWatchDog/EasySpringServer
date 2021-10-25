package i.watch.modules.install.service

import i.watch.modules.install.model.view.InstallInitResultView
import i.watch.modules.install.model.view.InstallInitView

interface InstallService {
    val installed: Boolean
    fun install(init: InstallInitView): InstallInitResultView
    fun checkInstall()
}
