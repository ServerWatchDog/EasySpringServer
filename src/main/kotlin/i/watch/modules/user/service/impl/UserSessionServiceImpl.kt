package i.watch.modules.user.service.impl

import i.watch.handler.security.session.ISessionService
import i.watch.modules.user.service.IUserService
import i.watch.modules.user.service.IUserSessionService
import i.watch.utils.TokenUtils
import i.watch.utils.cache.LightDB
import i.watch.utils.cache.createMap
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.concurrent.atomic.AtomicReference

@Service("AUTH:user")
class UserSessionServiceImpl(
    private val lightDB: LightDB,
    private val userService: IUserService
) : IUserSessionService, ISessionService.GenericISessionService<UserSession> {

    override fun createSessionByUserId(id: Long): String {
        val sessionId = AtomicReference("")
        val dbMap = lightDB.createMap {
            val token = TokenUtils.randomToken("user")
            sessionId.set(token)
            "session:user:$token"
        }
        UserSession(dbMap).apply {
            userId = id
        }
        return sessionId.get()
    }

    override fun getSession(token: String): Optional<UserSession> {
        return lightDB.getMap("session:user:$token").map {
            UserSession(it)
        }
    }

    override fun verifySession(session: UserSession, permissions: Array<String>): Boolean {
        return userService.getUserAuthorities(session.userId).containsAll(permissions.toList())
    }
}
