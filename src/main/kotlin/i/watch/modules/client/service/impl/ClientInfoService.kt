package i.watch.modules.client.service.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import i.watch.modules.client.model.db.QClientEntity
import i.watch.modules.client.model.view.online.OnlineItemView
import i.watch.modules.client.model.view.online.OnlineResultView
import i.watch.modules.client.service.IClientInfoService
import i.watch.modules.client.service.IClientService
import i.watch.modules.push.service.IPushService
import i.watch.modules.user.service.impl.UserSessionServiceImpl
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ClientInfoService(
    private val jpaQuery: JPAQueryFactory,
    private val clientService: IClientService,
    private val pushService: IPushService
) : IClientInfoService {
    override fun currentOnline(userSession: UserSessionServiceImpl.UserSession): OnlineResultView {
        val date = LocalDateTime.now().minusMinutes(5)
        return jpaQuery.select(QClientEntity.clientEntity)
            .from(QClientEntity.clientEntity)
            .where(QClientEntity.clientEntity.linkedUser.id.eq(userSession.userId))
            .fetch().map { Pair(it, clientService.getSessionByClientId(it.id)) }
            .filter { it.second.isPresent && it.second.get().enable }
            .map { Pair(it.first, it.second.get()) }
            .map {
                Pair(it.first, pushService.getPushSessionByToken(it.second.currentPushToken))
            }.filter { it.second.isPresent }
            .map { Pair(it.first, it.second.get()) }
            .filter { it.second.lastPushDate.isAfter(date) }
            .map {
                OnlineItemView(
                    name = it.first.name,
                    arch = it.second.clientInfo.arch,
                    system = it.second.clientInfo.system,
                    cpuName = it.second.clientInfo.cpuName,
                    cpuStage = it.second.clientStatus.cpuStage,
                    memory = it.second.clientInfo.memory,
                    usedMemory = it.second.clientStatus.usedMemory,
                    disk = it.second.clientInfo.disk,
                    usedDisk = it.second.clientStatus.usedDisk,
                    usedNetwork = it.second.clientStatus.usedNetwork

                )
            }.let {
                OnlineResultView(it)
            }
    }
}
