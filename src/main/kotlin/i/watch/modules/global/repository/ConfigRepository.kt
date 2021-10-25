package i.watch.modules.global.repository

import i.watch.modules.global.model.db.ConfigEntity
import i.watch.utils.querydsl.IRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ConfigRepository : IRepository<ConfigEntity, String> {
    fun getByKey(key: String): Optional<ConfigEntity>
}
