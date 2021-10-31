package i.watch.modules.client.repository

import i.watch.modules.client.model.db.ClientGroupEntity
import i.watch.utils.jpa.IRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientGroupRepository : IRepository<ClientGroupEntity, Long> {
    fun findAllByIdIn(ids: Set<Long>): Set<ClientGroupEntity>
}
