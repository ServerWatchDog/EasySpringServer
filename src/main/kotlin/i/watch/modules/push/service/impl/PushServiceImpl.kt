package i.watch.modules.push.service.impl

import i.watch.handler.inject.session.ISessionService
import i.watch.modules.client.model.session.ClientSession
import i.watch.modules.client.service.IClientService
import i.watch.modules.log.service.ILogService
import i.watch.modules.push.model.session.PushSession
import i.watch.modules.push.model.view.push.ClientLoginView
import i.watch.modules.push.model.view.push.ClientPushView
import i.watch.modules.push.service.IPushService
import i.watch.utils.TokenUtils
import i.watch.utils.cache.LightDB
import i.watch.utils.template.SimpleView
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import javax.annotation.Resource

@Service("AUTH:push")
class PushServiceImpl(
    private val lightDB: LightDB,
    private val clientService: IClientService
) : IPushService, ISessionService.GenericISessionService<PushSession> {

    @Resource
    lateinit var logService: ILogService
    override fun connect(clientLogin: ClientLoginView, clientSession: ClientSession): SimpleView<String> {
        val lastKey = "session:push:${clientSession.currentPushToken}"
        lightDB.getMap(lastKey).ifPresent {
            disConnect(PushSession(it))
            // 断开旧的会话
        }
        val newToken = TokenUtils.randomToken("push")
        val newKey = "session:push:$newToken"
        PushSession(lightDB.createMap(newKey)).apply {
            this.clientId = clientSession.clientId
            this.token = newToken
            this.clientOnlineId = logService.putClientInfo(clientSession.clientId, clientLogin)
            this.clientInfo = clientLogin
        }
        clientSession.currentPushToken = newToken
        return SimpleView(newToken)
    }

    override fun getSession(token: String): Optional<PushSession> {
        return lightDB.getMap("session:push:$token").map { PushSession(it) }
    }

    override fun verifySession(session: PushSession, permissions: Array<String>): Boolean {
        return clientService.getSessionByClientId(session.clientId).map {
            it.enable && it.currentPushToken == session.token
        }.orElse(false)
    }

    override fun push(clientPush: ClientPushView, pushSession: PushSession): SimpleView<Boolean> {
        if (pushSession.lastSaveDate.isBefore(LocalDateTime.now().minusMinutes(5))) {
            logService.putClientStatus(pushSession.clientId, pushSession.clientOnlineId, clientPush)
            pushSession.lastSaveDate = LocalDateTime.now()
        }
        pushSession.clientStatus = clientPush
        pushSession.lastPushDate = LocalDateTime.now()
        return SimpleView(true)
    }

    override fun getPushSessionByToken(currentPushToken: String): Optional<PushSession> {
        return lightDB.getMap("session:push:$currentPushToken")
            .map { PushSession(it) }
    }

    override fun disConnect(pushSession: PushSession): SimpleView<Boolean> {
        val lastKey = "session:push:${pushSession.token}"
        lightDB.getMap(lastKey).ifPresent {
            clientService.getSessionByClientId(pushSession.clientId).ifPresent {
                it.currentPushToken = ""
            }
            logService.putLogoutMessage(pushSession.clientId, pushSession.clientOnlineId)
            lightDB.drop(lastKey)
            // 断开会话
        }
        return SimpleView(true)
    }
}
