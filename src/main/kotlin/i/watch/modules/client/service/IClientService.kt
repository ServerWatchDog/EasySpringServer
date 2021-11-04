package i.watch.modules.client.service

import i.watch.handler.inject.session.ISessionService
import i.watch.modules.client.model.session.ClientSession
import i.watch.modules.client.model.view.client.ClientInsertView
import i.watch.modules.client.model.view.client.ClientResultView
import i.watch.utils.template.crud.CRUDService
import java.util.Optional

interface IClientService :
    CRUDService<ClientInsertView, ClientResultView, Long>,
    ISessionService.GenericISessionService<ClientSession> {
    fun getSessionByClientId(clientId: Long): Optional<ClientSession>
}
