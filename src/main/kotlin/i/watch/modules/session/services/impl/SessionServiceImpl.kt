package i.watch.modules.session.services.impl

import i.watch.modules.session.services.IGlobalSession
import i.watch.modules.session.services.IGlobalSessionService
import org.springframework.stereotype.Service

@Service
class SessionServiceImpl : IGlobalSessionService {
    private val sessionHeader = "Authorization"

    override fun getSessionByToken(func: (String) -> String): IGlobalSession {
        return GlobalSession(func(sessionHeader).replace("Bearer ", ""))
    }

    override fun verify(session: IGlobalSession, permissions: Array<String>): Boolean {
        return false
    }
}
