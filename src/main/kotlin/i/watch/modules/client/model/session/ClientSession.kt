package i.watch.modules.client.model.session

import i.watch.utils.cache.LightDBMap
import i.watch.utils.cache.LightDBSession
import i.watch.utils.cache.bind
import java.util.concurrent.TimeUnit

class ClientSession(
    lightDBMap: LightDBMap
) : LightDBSession(lightDBMap, 120, TimeUnit.DAYS) {
    var currentPushToken by lightDBMap.bind("current.push.token", "")
    var enable by lightDBMap.bind<Boolean>("enable")
    var clientId by lightDBMap.bind<Long>("client-id")
}
