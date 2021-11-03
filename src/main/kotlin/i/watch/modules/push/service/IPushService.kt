package i.watch.modules.push.service

import i.watch.modules.client.model.view.client.ClientInfoView
import i.watch.modules.push.model.view.online.OnlineResultView
import i.watch.modules.push.model.view.push.ServerStatusResultView
import i.watch.modules.push.model.view.push.ServerStatusView
import i.watch.modules.push.service.impl.PushSessionServiceImpl
import i.watch.modules.user.service.impl.UserSessionServiceImpl

interface IPushService {
    fun newSession(clientId: Long, clientInfo: ClientInfoView): String
    fun push(pushSession: PushSessionServiceImpl.PushSession, serverStatusView: ServerStatusView): ServerStatusResultView
    fun getOnline(userSession: UserSessionServiceImpl.UserSession): OnlineResultView
}
