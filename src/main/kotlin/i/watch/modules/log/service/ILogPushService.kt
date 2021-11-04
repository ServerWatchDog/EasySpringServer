package i.watch.modules.log.service

import i.watch.modules.client.model.db.ClientEntity
import i.watch.modules.log.model.view.LogResultView
import i.watch.modules.user.model.db.UserEntity
import i.watch.utils.template.PageView
import org.springframework.data.domain.Pageable

interface ILogPushService {
    fun pushUserLog(userId: Long, message: String, vararg tags: String)
    fun pushUserLog(user: UserEntity, message: String, vararg tags: String)
    fun pushClientLog(clientId: Long, message: String, vararg tags: String)
    fun pushClientLog(client: ClientEntity, message: String, vararg tags: String)
    fun getUserLog(page: Pageable, id: Long, date: String): PageView<LogResultView>
    fun getClientLog(page: Pageable, id: Long, date: String): PageView<LogResultView>
}
