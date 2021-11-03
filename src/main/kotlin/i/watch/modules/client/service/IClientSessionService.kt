package i.watch.modules.client.service

import i.watch.handler.inject.session.ISessionService
import i.watch.modules.client.service.impl.ClientSession

interface IClientSessionService : ISessionService {
    /**
     * Create Client Session
     * @return String
     */
    fun newToken(): String

    fun getSessionById(id: Long): ClientSession
}
