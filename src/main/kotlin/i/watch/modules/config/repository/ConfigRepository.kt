package i.watch.modules.config.repository

import i.watch.modules.config.model.db.ConfigEntity
import i.watch.utils.jpa.IRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ConfigRepository : IRepository<ConfigEntity, String> {
    fun getByKey(key: String): Optional<ConfigEntity>
}
