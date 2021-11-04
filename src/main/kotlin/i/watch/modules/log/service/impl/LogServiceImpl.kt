package i.watch.modules.log.service.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import i.watch.handler.advice.NotFoundException
import i.watch.modules.client.model.db.QClientEntity
import i.watch.modules.client.service.IClientService
import i.watch.modules.log.model.db.ClientInfoEntity
import i.watch.modules.log.model.db.ClientOnlineEntity
import i.watch.modules.log.model.db.ClientStatusEntity
import i.watch.modules.log.model.db.QClientInfoEntity
import i.watch.modules.log.model.db.QClientOnlineEntity
import i.watch.modules.log.model.db.QClientStatusEntity
import i.watch.modules.log.repository.ClientInfoRepository
import i.watch.modules.log.repository.ClientOnlineRepository
import i.watch.modules.log.repository.ClientStatusRepository
import i.watch.modules.log.service.ILogService
import i.watch.modules.push.model.view.push.ClientLoginView
import i.watch.modules.push.model.view.push.ClientPushView
import i.watch.modules.push.service.IPushService
import i.watch.utils.SnowFlakeUtils
import i.watch.utils.getLogger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import javax.annotation.Resource

@Service
class LogServiceImpl(
    private val clientInfoRepository: ClientInfoRepository,
    private val clientOnlineRepository: ClientOnlineRepository,
    private val clientStatusRepository: ClientStatusRepository,
    private val jpaQuery: JPAQueryFactory,
    private val idGenerate: SnowFlakeUtils
) : ILogService {
    override fun putClientInfo(clientId: Long, clientInfo: ClientLoginView): Long {
        val qCI = QClientInfoEntity.clientInfoEntity
        val client = jpaQuery.selectFrom(QClientEntity.clientEntity)
            .where(QClientEntity.clientEntity.id.eq(clientId)).fetchFirst() ?: throw NotFoundException("无此客户端")
        val infoEntity = jpaQuery.select(qCI).from(qCI).where(qCI.client.eq(client))
            .orderBy(qCI.createDate.desc()).fetchFirst()
            .let { Optional.ofNullable(it) }
            .orElseGet {
                clientInfoRepository.save(
                    ClientInfoEntity(
                        id = idGenerate.nextId(),
                        client = client,
                        arch = clientInfo.arch,
                        system = clientInfo.system,
                        cpuName = clientInfo.cpuName,
                        cpuCore = clientInfo.cpuCore,
                        memory = clientInfo.memory,
                        disk = clientInfo.disk,
                        createDate = LocalDateTime.now()
                    )
                )
            }.let {
                if (
                    it.arch != clientInfo.arch ||
                    it.system != clientInfo.system ||
                    it.cpuName != clientInfo.cpuName ||
                    it.cpuCore != clientInfo.cpuCore ||
                    it.memory != clientInfo.memory ||
                    it.disk != clientInfo.disk
                ) {
                    clientInfoRepository.save(
                        ClientInfoEntity(
                            id = idGenerate.nextId(),
                            client = client,
                            arch = clientInfo.arch,
                            system = clientInfo.system,
                            cpuName = clientInfo.cpuName,
                            cpuCore = clientInfo.cpuCore,
                            memory = clientInfo.memory,
                            disk = clientInfo.disk,
                            createDate = LocalDateTime.now()
                        )
                    )
                } else {
                    it
                }
            }
        return clientOnlineRepository.save(
            ClientOnlineEntity(
                id = idGenerate.nextId(),
                client,
                LocalDateTime.now(),
                infoEntity
            )
        ).id
    }

    override fun putClientStatus(clientId: Long, clientOnlineId: Long, clientPush: ClientPushView) {
        val online = jpaQuery.selectFrom(QClientOnlineEntity.clientOnlineEntity)
            .where(QClientOnlineEntity.clientOnlineEntity.id.eq(clientOnlineId)).fetchFirst()
            ?: throw NotFoundException("此客户端未注册在线")
        val qCS = QClientStatusEntity.clientStatusEntity
        val lastLog = jpaQuery.selectFrom(qCS).where(qCS.onlineEntity.eq(online))
            .orderBy(qCS.putDate.desc()).fetchFirst()
        if (lastLog == null || lastLog.putDate.isBefore(LocalDateTime.now().minusMinutes(5))) {
            clientStatusRepository.save(
                ClientStatusEntity(
                    idGenerate.nextId(),
                    online,
                    cpuStage = clientPush.cpuStage,
                    usedMemory = clientPush.usedMemory,
                    usedDisk = clientPush.usedDisk,
                    usedNetwork = clientPush.usedNetwork,
                    LocalDateTime.now()
                )
            )
        }
    }

    @Resource
    lateinit var pushService: IPushService

    @Resource
    lateinit var clientService: IClientService

    private val logger = getLogger()

    @Scheduled(fixedRate = 60_000)
    fun destroyLogout() {
        logger.debug("销毁过时会话.")
        val before = LocalDateTime.now().minusMinutes(5)
        val clients = jpaQuery.select(QClientEntity.clientEntity.id).from(QClientEntity.clientEntity)
            .where(QClientEntity.clientEntity.enable.eq(true))
            .fetch()
        clients.asSequence().map { clientService.getSessionByClientId(it) }
            .filter { it.isPresent }
            .map { pushService.getPushSessionByToken(it.get().currentPushToken) }
            .filter { it.isPresent }
            .map { it.get() }
            .filter { it.lastPushDate.isBefore(before) }.toList()
            .forEach { pushService.disConnect(it) }
    }

    override fun putLogoutMessage(clientId: Long, clientOnlineId: Long) {

    }
}
