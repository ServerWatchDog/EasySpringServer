package i.watch.modules.client.service.impl

import i.watch.handler.inject.session.ISessionService
import i.watch.modules.client.ClientAuthority
import i.watch.modules.client.repository.ClientRepository
import i.watch.modules.client.service.IClientSessionService
import i.watch.utils.TokenUtils
import org.springframework.stereotype.Service
import java.util.Optional

@Service("AUTH:cli")
class ClientSessionServiceImpl(
    private val clientRepository: ClientRepository,
) :
    IClientSessionService,
    ISessionService.GenericISessionService<ClientSession> {
    override fun getSession(token: String): Optional<ClientSession> {
        return clientRepository.findByToken(token).map {
            ClientSession(it.enable)
        }
    }

    override fun verifySession(session: ClientSession, permissions: Array<String>): Boolean {
        if (permissions.contains(ClientAuthority.ENABLED) && session.enable.not()) {
            return false
        }
        return true
    }

    override fun newToken(): String {
        return TokenUtils.randomToken("cli")
    }
}
