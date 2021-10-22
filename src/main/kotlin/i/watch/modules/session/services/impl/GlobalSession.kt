package i.watch.modules.session.services.impl

import i.watch.modules.session.services.IGlobalSession

class GlobalSession(private val token: String) : IGlobalSession {
    override fun refresh() {
        println("Refresh")
    }
}
