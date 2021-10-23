package i.watch.utils.querydsl

import org.springframework.data.repository.NoRepositoryBean
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@NoRepositoryBean
abstract class BaseRepository {
    @PersistenceContext
    protected lateinit var entityManager: EntityManager
}
