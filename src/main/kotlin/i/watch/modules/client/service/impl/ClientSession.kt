package i.watch.modules.client.service.impl

import i.watch.utils.cache.LightDBMap
import i.watch.utils.cache.LightDBSession
import i.watch.utils.cache.bind
import java.util.concurrent.TimeUnit

class ClientSession(
    lightDBMap: LightDBMap
) : LightDBSession(lightDBMap, 120, TimeUnit.DAYS) {

    var lastPushDate by lightDBMap.bind<Long>("last.push.date", 0)
    var enable: Boolean by lightDBMap.bind("enable")
    var clientId: Long by lightDBMap.bind("clientId")
    var lastProfileId: Long by lightDBMap.bind("last.profile")
    var lastToken: String by lightDBMap.bind("last.session", "")
}
