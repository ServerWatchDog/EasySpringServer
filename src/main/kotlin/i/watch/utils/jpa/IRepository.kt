package i.watch.utils.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface IRepository<T, ID> :
    JpaRepository<T, ID>,
    JpaSpecificationExecutor<T>,
    QuerydslPredicateExecutor<T>
