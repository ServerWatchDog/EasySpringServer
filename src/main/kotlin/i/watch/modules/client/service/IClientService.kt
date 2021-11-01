package i.watch.modules.client.service

import i.watch.modules.client.model.view.client.ClientInfoView
import i.watch.modules.client.model.view.client.ClientInsertView
import i.watch.modules.client.model.view.client.ClientLoginResultView
import i.watch.modules.client.model.view.client.ClientResultView
import i.watch.modules.client.service.impl.ClientSession
import i.watch.utils.template.crud.CRUDService

interface IClientService : CRUDService<ClientInsertView, ClientResultView, Long> {
    fun clientLogin(clientSession: ClientSession, clientInfo: ClientInfoView): ClientLoginResultView
}
