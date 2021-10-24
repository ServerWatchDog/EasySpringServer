package i.watch.modules.user.repository

import i.watch.modules.user.model.db.UserEntity
import i.watch.utils.querydsl.IRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : IRepository<UserEntity, String>
