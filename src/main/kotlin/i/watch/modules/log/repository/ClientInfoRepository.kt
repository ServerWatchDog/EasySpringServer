package i.watch.modules.log.repository

import i.watch.modules.log.model.db.ClientInfoEntity
import i.watch.utils.jpa.IRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientInfoRepository : IRepository<ClientInfoEntity, Long>
