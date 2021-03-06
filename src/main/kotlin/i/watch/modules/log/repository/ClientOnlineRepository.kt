package i.watch.modules.log.repository

import i.watch.modules.log.model.db.ClientOnlineEntity
import i.watch.utils.jpa.IRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientOnlineRepository : IRepository<ClientOnlineEntity, Long>
