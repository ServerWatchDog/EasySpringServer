package i.watch.modules.install.service

import i.watch.modules.install.model.view.InstallInitResultView
import i.watch.modules.install.model.view.InstallInitView

interface InstallService {
    fun install(init: InstallInitView): InstallInitResultView
    fun checkInstall()
}
