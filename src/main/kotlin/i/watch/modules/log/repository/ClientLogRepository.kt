package i.watch.modules.log.repository

import i.watch.modules.log.model.db.ClientLogEntity
import i.watch.utils.jpa.IRepository

interface ClientLogRepository : IRepository<ClientLogEntity, Long>
