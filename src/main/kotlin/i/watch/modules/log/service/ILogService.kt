package i.watch.modules.log.service

import i.watch.modules.push.model.view.push.ClientLoginView
import i.watch.modules.push.model.view.push.ClientPushView

interface ILogService {
    fun putClientInfo(clientId: Long, clientInfo: ClientLoginView): Long
    fun putClientStatus(clientId: Long, clientOnlineId: Long, clientPush: ClientPushView)
    fun putLogoutMessage(clientId: Long, clientOnlineId: Long)
}
