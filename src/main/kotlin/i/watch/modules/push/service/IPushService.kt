package i.watch.modules.push.service

import i.watch.modules.client.model.view.client.ClientInfoView

interface IPushService {
    fun newSession(clientId: Long, clientInfo: ClientInfoView): String
}
