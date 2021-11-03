package i.watch.modules.client.service.impl

import i.watch.handler.inject.session.ISessionService
import i.watch.modules.client.ClientAuthority
import i.watch.modules.client.repository.ClientRepository
import i.watch.modules.client.service.IClientSessionService
import i.watch.utils.TokenUtils
import i.watch.utils.cache.LightDB
import org.springframework.stereotype.Service
import java.util.Optional

@Service("AUTH:cli")
class ClientSessionServiceImpl(
    private val clientRepository: ClientRepository,
    private val lightDB: LightDB
) :
    IClientSessionService,
    ISessionService.GenericISessionService<ClientSession> {
    override fun getSession(token: String): Optional<ClientSession> {
        return clientRepository.findByToken(token).map {
            val clientSession = getSessionById(it.id)
            clientSession.clientId = it.id
            clientSession.enable = it.enable
            clientSession
        }
    }

    override fun verifySession(session: ClientSession, permissions: Array<String>): Boolean {
        if (permissions.contains(ClientAuthority.ENABLED) && session.enable.not()) {
            return false
        }
        return true
    }

    override fun getSessionById(id: Long): ClientSession {
        val orCreateMap = lightDB.getOrCreateMap("save:client:$id")
        return ClientSession(orCreateMap)
    }

    override fun newToken(): String {
        return TokenUtils.randomToken("cli")
    }
}
