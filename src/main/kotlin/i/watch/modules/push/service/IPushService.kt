package i.watch.modules.push.service

import i.watch.modules.client.model.session.ClientSession
import i.watch.modules.push.model.session.PushSession
import i.watch.modules.push.model.view.push.ClientLoginView
import i.watch.modules.push.model.view.push.ClientPushView
import i.watch.utils.template.SimpleView

interface IPushService {
    /**
     * 终端登录
     */
    fun connect(clientLogin: ClientLoginView, clientSession: ClientSession): SimpleView<String>

    /**
     * 消息上报
     */
    fun push(clientPush: ClientPushView, pushSession: PushSession): SimpleView<Boolean>

    /**
     * 会话终止
     */
    fun disConnect(pushSession: PushSession): SimpleView<Boolean>
}
