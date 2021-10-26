package i.watch.modules.user.service.impl

import i.watch.utils.cache.LightDBMap
import i.watch.utils.cache.LightDBSession
import i.watch.utils.cache.bind
import java.util.concurrent.TimeUnit

class UserSession(map: LightDBMap) :
    LightDBSession(map, 1, TimeUnit.DAYS) {
    var userId by map.bind<Long>("userId")
}
