package i.watch.modules.client.service.impl

import i.watch.handler.inject.session.ISession

class ClientSession(
    val enable: Boolean,
) : ISession {
    override fun refresh() {
    }
}
