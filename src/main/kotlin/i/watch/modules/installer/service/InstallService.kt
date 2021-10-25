package i.watch.modules.installer.service

import i.watch.modules.installer.model.view.InstallInitResultView
import i.watch.modules.installer.model.view.InstallInitView

interface InstallService {
    val installed: Boolean
    fun install(init: InstallInitView): InstallInitResultView
    fun checkInstall()
}
