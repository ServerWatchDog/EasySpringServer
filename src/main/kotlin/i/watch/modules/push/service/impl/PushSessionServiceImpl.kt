package i.watch.modules.push.service.impl

import i.watch.handler.inject.session.ISessionService
import i.watch.modules.push.service.IPushSessionService
import i.watch.utils.cache.LightDB
import i.watch.utils.cache.LightDBMap
import i.watch.utils.cache.LightDBSession
import i.watch.utils.cache.bind
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.concurrent.TimeUnit

@Service("AUTH:push")
class PushSessionServiceImpl(
    private val lightDB: LightDB
) :
    IPushSessionService,
    ISessionService.GenericISessionService<PushSessionServiceImpl.PushSession> {

    override fun getSession(token: String): Optional<PushSession> {
        return lightDB.getMap("session:push:$token").map {
            val pushSession = PushSession(it)
            pushSession.token = token
            pushSession
        }
    }

    override fun verifySession(session: PushSession, permissions: Array<String>): Boolean {
        return permissions.contains("push-data")
    }

    class PushSession(lightDBMap: LightDBMap) : LightDBSession(lightDBMap, 5, TimeUnit.MINUTES) {
        var token: String by lightDBMap.bind("name")
        var clientId by lightDBMap.bind<Long>("client.id")
    }
}
