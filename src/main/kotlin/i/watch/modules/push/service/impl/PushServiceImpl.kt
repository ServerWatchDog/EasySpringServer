package i.watch.modules.push.service.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import i.watch.handler.advice.ForbiddenException
import i.watch.handler.advice.NotFoundException
import i.watch.modules.client.model.db.QClientEntity
import i.watch.modules.client.model.view.client.ClientInfoView
import i.watch.modules.client.service.IClientSessionService
import i.watch.modules.log.model.db.ClientInfoEntity
import i.watch.modules.log.model.db.ClientOnlineEntity
import i.watch.modules.log.model.db.ClientStatusEntity
import i.watch.modules.log.model.db.QClientInfoEntity
import i.watch.modules.log.model.db.QClientOnlineEntity
import i.watch.modules.log.model.db.QClientStatusEntity
import i.watch.modules.log.model.view.ClientInfoRepository
import i.watch.modules.log.model.view.ClientOnlineRepository
import i.watch.modules.log.model.view.ClientStatusRepository
import i.watch.modules.push.model.view.online.OnlineItemView
import i.watch.modules.push.model.view.online.OnlineResultView
import i.watch.modules.push.model.view.push.ServerStatusResultView
import i.watch.modules.push.model.view.push.ServerStatusView
import i.watch.modules.push.service.IPushService
import i.watch.modules.user.service.impl.UserSessionServiceImpl
import i.watch.utils.SnowFlakeUtils
import i.watch.utils.TokenUtils
import i.watch.utils.cache.LightDB
import i.watch.utils.cache.createMap
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Service
class PushServiceImpl(
    private val lightDB: LightDB,
    private val jpaQuery: JPAQueryFactory,
    private val idGenerator: SnowFlakeUtils,
    private val clientSessionService: IClientSessionService,
    private val clientInfoRepository: ClientInfoRepository,
    private val clientStatusRepository: ClientStatusRepository,
    private val clientOnlineRepository: ClientOnlineRepository
) : IPushService {
    override fun newSession(clientId: Long, clientInfo: ClientInfoView): String {
        val clientEntity = jpaQuery.selectFrom(QClientEntity.clientEntity)
            .where(QClientEntity.clientEntity.id.eq(clientId))
            .fetchOne() ?: throw NotFoundException("此设备不存在.")
        val clientSession = clientSessionService.getSessionById(clientId)
        // 客户端
        var token = ""
        val pushSessionMap = lightDB.createMap {
            token = TokenUtils.randomToken("push")
            "session:push:$token"
        } // 推送Token
        pushSessionMap.expire(5, TimeUnit.MINUTES)
        val pushSession = PushSessionServiceImpl.PushSession(pushSessionMap)
        val oldPushToken = clientSession.lastToken
        clientSession.lastToken = token
        pushSession.clientId = clientId
        try {
            val newInfo = ClientInfoEntity(
                id = idGenerator.nextId(),
                arch = clientInfo.arch,
                system = clientInfo.system,
                cpuName = clientInfo.cpuName,
                cpuCore = clientInfo.cpuCore,
                memory = clientInfo.memory,
                disk = clientInfo.disk,
                createDate = LocalDateTime.now(),
                client = clientEntity
            )
            val qCI = QClientInfoEntity.clientInfoEntity
            val currentInfo = jpaQuery.selectFrom(qCI)
                .where(qCI.client.eq(clientEntity))
                .orderBy(qCI.createDate.desc())
                .fetchFirst() ?: newInfo
            if (currentInfo.arch == clientInfo.arch &&
                currentInfo.system == clientInfo.system &&
                currentInfo.cpuName == clientInfo.cpuName &&
                currentInfo.cpuCore == clientInfo.cpuCore &&
                currentInfo.memory == clientInfo.memory &&
                currentInfo.disk == clientInfo.disk &&
                newInfo != currentInfo
            ) {
                // 配置未变
                clientSession.lastProfileId = currentInfo.id
            } else {
                clientInfoRepository.save(newInfo)
                clientSession.lastProfileId = newInfo.id
            }
        } catch (e: Exception) {
            if (oldPushToken.isNotBlank()) {
                clientSession.lastToken = oldPushToken
            } // 回滚
            throw e
        }
        return token
    }

    override fun push(
        pushSession: PushSessionServiceImpl.PushSession,
        serverStatusView: ServerStatusView
    ): ServerStatusResultView {
        val sessionById = clientSessionService.getSessionById(pushSession.clientId)
        if (sessionById.lastToken != pushSession.token) {
            throw ForbiddenException("此会话已过期，请重新上线")
        }
        if (sessionById.lastPushDate > System.currentTimeMillis() - 3 * 1000 * 60) {
            // 时间过少
        } else {
            // 时间可以
            sessionById.lastPushDate = System.currentTimeMillis()
            val online = clientOnlineRepository.save(
                ClientOnlineEntity(
                    id = idGenerator.nextId(),
                    linkedClient = jpaQuery.selectFrom(QClientEntity.clientEntity)
                        .where(QClientEntity.clientEntity.id.eq(pushSession.clientId)).fetchOne()!!,
                    date = LocalDateTime.now()

                )
            )
            clientStatusRepository.save(
                ClientStatusEntity(
                    id = idGenerator.nextId(),
                    cpuStage = serverStatusView.cpuStage,
                    usedMemory = serverStatusView.usedMemory,
                    usedDisk = serverStatusView.usedDisk,
                    usedNetwork = serverStatusView.usedNetwork,
                    onlineEntity = online,
                    linkedInfo = clientInfoRepository.getById(sessionById.lastProfileId)
                )
            )
        }
        return ServerStatusResultView(true)
    }

    override fun getOnline(userSession: UserSessionServiceImpl.UserSession): OnlineResultView {
        val qCS = QClientStatusEntity.clientStatusEntity
        val qCL = QClientOnlineEntity.clientOnlineEntity
        val qC = QClientEntity.clientEntity
        val clients = jpaQuery.selectFrom(qC)
            .where(qC.linkedUser.id.eq(userSession.userId)).fetch()
        val online = jpaQuery.selectFrom(qCL).where(
            qCL.date.after(LocalDateTime.now().minusMinutes(5))
                .and(qCL.linkedClient.`in`(clients))
        ).orderBy(qCL.date.desc()).groupBy(qCL.linkedClient).fetch() ?: return OnlineResultView(emptyList())

        return jpaQuery.select(qCS.linkedInfo, qCS.onlineEntity.linkedClient, qCS).from(qCS)
            .where(qCS.onlineEntity.`in`(online)).fetch()
            .map {
                val info = it.get(qCS.linkedInfo)!!
                val client = it.get(qCS.onlineEntity.linkedClient)!!
                val status = it.get(qCS)!!
                OnlineItemView(
                    client.name,
                    info.arch,
                    info.system,
                    info.cpuName,
                    status.cpuStage,
                    info.memory,
                    status.usedMemory,
                    info.disk,
                    status.usedDisk,
                    status.usedNetwork
                )
            }.let { OnlineResultView(it) }
    }
}
