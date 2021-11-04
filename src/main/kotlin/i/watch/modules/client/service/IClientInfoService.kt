package i.watch.modules.client.service

import i.watch.modules.client.model.view.online.OnlineResultView
import i.watch.modules.user.service.impl.UserSessionServiceImpl

interface IClientInfoService {
    fun currentOnline(userSession: UserSessionServiceImpl.UserSession): OnlineResultView
}
