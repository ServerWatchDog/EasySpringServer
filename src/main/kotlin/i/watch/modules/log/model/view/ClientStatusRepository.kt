package i.watch.modules.log.model.view

import i.watch.modules.log.model.db.ClientStatusEntity
import i.watch.utils.jpa.IRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientStatusRepository : IRepository<ClientStatusEntity, Long>
