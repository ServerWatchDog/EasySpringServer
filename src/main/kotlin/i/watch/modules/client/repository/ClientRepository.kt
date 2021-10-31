package i.watch.modules.client.repository

import i.watch.modules.client.model.db.ClientEntity
import i.watch.utils.jpa.IRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ClientRepository : IRepository<ClientEntity, Long> {
    fun findByToken(token: String): Optional<ClientEntity>
}
