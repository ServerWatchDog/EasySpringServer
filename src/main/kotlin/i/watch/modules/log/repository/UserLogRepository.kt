package i.watch.modules.log.repository

import i.watch.modules.log.model.db.ClientStatusEntity
import i.watch.modules.log.model.db.UserLogEntity
import i.watch.utils.jpa.IRepository


interface UserLogRepository : IRepository<UserLogEntity, Long>
