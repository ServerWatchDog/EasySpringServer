package i.watch.modules.log.service.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import i.watch.modules.client.model.db.ClientEntity
import i.watch.modules.client.repository.ClientRepository
import i.watch.modules.log.model.db.ClientLogEntity
import i.watch.modules.log.model.db.LogTagEntity
import i.watch.modules.log.model.db.QClientLogEntity
import i.watch.modules.log.model.db.QUserLogEntity
import i.watch.modules.log.model.db.UserLogEntity
import i.watch.modules.log.model.view.LogResultView
import i.watch.modules.log.repository.ClientLogRepository
import i.watch.modules.log.repository.LogTagRepository
import i.watch.modules.log.repository.UserLogRepository
import i.watch.modules.log.service.ILogPushService
import i.watch.modules.user.model.db.UserEntity
import i.watch.modules.user.repository.UserRepository
import i.watch.utils.DateTimeUtils
import i.watch.utils.SnowFlakeUtils
import i.watch.utils.getLogger
import i.watch.utils.template.PageView
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class LogPushServiceImpl(
    private val jpaQuery: JPAQueryFactory,
    private val userRepository: UserRepository,
    private val clientRepository: ClientRepository,
    private val clientLogRepository: ClientLogRepository,
    private val logTagRepository: LogTagRepository,
    private val userLogRepository: UserLogRepository,
    private val idGenerator: SnowFlakeUtils
) : ILogPushService {
    private val logger = getLogger()
    override fun pushUserLog(user: UserEntity, message: String, vararg tags: String) {
        val map = tags.toList().map { LogTagEntity(it) }
        try {
            logTagRepository.saveAll(map)
        } catch (e: Exception) {
            logger.debug("已知错误：{}", e.message)
        }
        val userLogEntity = UserLogEntity(
            idGenerator.nextId(),
            user,
            message,
            LocalDateTime.now()
        )
        userLogEntity.tags.addAll(map)
        userLogRepository.save(
            userLogEntity
        )
    }

    override fun pushClientLog(client: ClientEntity, message: String, vararg tags: String) {
        val map = tags.toList().map { LogTagEntity(it) }
        try {
            logTagRepository.saveAll(map)
        } catch (e: Exception) {
            logger.debug("已知错误：{}", e.message)
        }
        val entity = ClientLogEntity(
            idGenerator.nextId(),
            client,
            message,
            LocalDateTime.now()
        )
        entity.tags.addAll(map)
        clientLogRepository.save(
            entity
        )
    }

    override fun pushUserLog(userId: Long, message: String, vararg tags: String) {
        pushUserLog(userRepository.findById(userId).get(), message, *tags)
    }

    override fun pushClientLog(clientId: Long, message: String, vararg tags: String) {
        pushClientLog(clientRepository.findById(clientId).get(), message, *tags)
    }

    override fun getClientLog(page: Pageable, id: Long, date: String): PageView<LogResultView> {
        val before = DateTimeUtils.parseDate(date)
        val where = jpaQuery.selectFrom(QClientLogEntity.clientLogEntity).where(
            QClientLogEntity.clientLogEntity.date.before(before)
                .and(QClientLogEntity.clientLogEntity.linkedClient.id.eq(id))
        )
        return where.offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch().map {
                LogResultView(
                    it.linkedClient.name,
                    it.message,
                    it.tags.joinToString { item -> item.id },
                    DateTimeUtils.formatDateTime(it.date)
                )
            }.let {
                val size = where.fetchCount()
                PageView(
                    it,
                    pageIndex = page.pageNumber + 1,
                    pageCount = (size / page.pageSize) + 1,
                    size = size
                )
            }
    }

    @Suppress("DuplicatedCode")
    override fun getUserLog(page: Pageable, id: Long, date: String): PageView<LogResultView> {
        val before = DateTimeUtils.parseDate(date)
        val where = jpaQuery.selectFrom(QUserLogEntity.userLogEntity).where(
            QUserLogEntity.userLogEntity.date.before(before)
                .and(QUserLogEntity.userLogEntity.linkedUser.id.eq(id))
        )
        return where.offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch().map {
                LogResultView(
                    it.linkedUser.name,
                    it.message,
                    it.tags.joinToString { item -> item.id },
                    DateTimeUtils.formatDateTime(it.date)
                )
            }.let {
                val size = where.fetchCount()
                PageView(
                    it,
                    pageIndex = page.pageNumber + 1,
                    pageCount = (size / page.pageSize) + 1,
                    size = size
                )
            }
    }
}
