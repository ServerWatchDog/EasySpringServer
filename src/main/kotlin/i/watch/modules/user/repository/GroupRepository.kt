package i.watch.modules.user.repository

import i.watch.modules.user.model.db.GroupEntity
import i.watch.utils.jpa.IRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository : IRepository<GroupEntity, Long>
