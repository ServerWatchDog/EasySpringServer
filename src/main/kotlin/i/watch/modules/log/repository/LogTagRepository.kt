package i.watch.modules.log.repository

import i.watch.modules.log.model.db.LogTagEntity
import i.watch.utils.jpa.IRepository
import org.springframework.stereotype.Repository

@Repository
interface LogTagRepository : IRepository<LogTagEntity, Long>
