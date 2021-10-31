package i.watch.modules.client.service

import i.watch.handler.inject.session.ISessionService

interface IClientSessionService : ISessionService {
    /**
     * Create Client Session
     * @return String
     */
    fun newToken(): String
}
