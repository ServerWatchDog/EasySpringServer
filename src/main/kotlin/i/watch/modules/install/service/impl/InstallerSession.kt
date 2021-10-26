package i.watch.modules.install.service.impl

import i.watch.utils.cache.LightDBMap
import i.watch.utils.cache.LightDBSession
import java.util.concurrent.TimeUnit

class InstallerSession(lightDBMap: LightDBMap) :
    LightDBSession(lightDBMap, 1, TimeUnit.DAYS)
