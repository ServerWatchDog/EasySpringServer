package i.watch.modules.user.repository

import i.watch.modules.user.model.db.AuthorityEntity
import i.watch.utils.querydsl.IRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorityRepository : IRepository<AuthorityEntity, String>
