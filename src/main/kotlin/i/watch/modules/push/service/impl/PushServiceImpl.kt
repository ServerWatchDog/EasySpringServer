package i.watch.modules.push.service.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import i.watch.handler.advice.NotFoundException
import i.watch.modules.client.model.db.QClientEntity
import i.watch.modules.client.model.view.client.ClientInfoView
import i.watch.modules.log.model.db.ClientInfoEntity
import i.watch.modules.log.model.db.QClientInfoEntity
import i.watch.modules.log.model.view.ClientInfoRepository
import i.watch.modules.push.service.IPushService
import i.watch.utils.SnowFlakeUtils
import i.watch.utils.TokenUtils
import i.watch.utils.cache.LightDB
import i.watch.utils.cache.createMap
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Service
class PushServiceImpl(
    private val lightDB: LightDB,
    private val jpaQuery: JPAQueryFactory,
    private val idGenerator: SnowFlakeUtils,
    private val clientInfoRepository: ClientInfoRepository
) : IPushService {
    override fun newSession(clientId: Long, clientInfo: ClientInfoView): String {
        val clientEntity = jpaQuery.selectFrom(QClientEntity.clientEntity)
            .where(QClientEntity.clientEntity.id.eq(clientId))
            .fetchOne() ?: throw NotFoundException("此设备不存在.")
        val clientSession = lightDB.getOrCreateMap("save:client:${clientEntity.id}")
        // 客户端
        var token = ""
        val pushSession = lightDB.createMap {
            token = TokenUtils.randomToken("push")
            "session:push:$token"
        } // 推送Token
        pushSession.expire(5, TimeUnit.MINUTES)
        val oldPushToken = clientSession.get("current.token", String::class)
        clientSession.putValue("current.token", token)
        pushSession.putValue("client.id", clientId)
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
                clientSession.putValue("current.profile.id", currentInfo.id)
            } else {
                clientInfoRepository.save(newInfo)
                clientSession.putValue("current.profile.id", newInfo.id)
            }
        } catch (e: Exception) {
            oldPushToken.ifPresent {
                clientSession.putValue("current.token", it) // 回滚
            }
            throw e
        }
        return token
    }
}
