package i.watch.modules.push.model.session

import i.watch.modules.push.model.view.push.ClientLoginView
import i.watch.modules.push.model.view.push.ClientPushView
import i.watch.utils.cache.LightDBMap
import i.watch.utils.cache.LightDBSession
import i.watch.utils.cache.bind
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class PushSession(lightDBMap: LightDBMap) :
    LightDBSession(lightDBMap, 1, TimeUnit.HOURS) {
    var lastPushDate by lightDBMap.bind<LocalDateTime>("client.push.date", LocalDateTime.MIN)
    var lastSaveDate by lightDBMap.bind<LocalDateTime>("client.push.date", LocalDateTime.MIN)
    var clientStatus by bind<ClientPushView>("client.status")
    var clientInfo by bind<ClientLoginView>("client.info")
    var clientOnlineId by bind<Long>("client.online.id")
    var token by bind<String>("token")
    var clientId by bind<Long>("client.id")
}
