package i.watch.modules.user.service.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import i.watch.handler.advice.BadRequestException
import i.watch.modules.user.model.db.GroupEntity
import i.watch.modules.user.model.db.QAuthorityEntity
import i.watch.modules.user.model.db.QUserEntity
import i.watch.modules.user.model.view.group.GroupInsertView
import i.watch.modules.user.model.view.group.GroupResultView
import i.watch.modules.user.repository.AuthorityRepository
import i.watch.modules.user.repository.GroupRepository
import i.watch.modules.user.service.IGroupService
import i.watch.utils.SnowFlakeUtils
import i.watch.utils.cache.cache.IDataCacheManager
import i.watch.utils.template.crud.CRUDServiceImpl
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class GroupServiceImpl(
    override val repository: GroupRepository,
    private val idGenerate: SnowFlakeUtils,
    private val jpaQuery: JPAQueryFactory,
    private val authorityRepository: AuthorityRepository,
    dataCacheManager: IDataCacheManager
) : IGroupService,
    CRUDServiceImpl<GroupInsertView, GroupResultView, Long, GroupEntity>() {
    private val authorityCache = dataCacheManager.newCache("authority")

    override fun tableToOutput(table: GroupEntity): GroupResultView {
        val qUser = QUserEntity.userEntity
        val userList = jpaQuery.select(qUser.id, qUser.name, qUser.email)
            .from(qUser)
            .where(qUser.linkGroup.eq(table))
            .fetch()
            .map {
                GroupResultView.GroupUserResultView(
                    id = it.get(qUser.id)!!.toString(),
                    name = it.get(qUser.name)!!,
                    email = it.get(qUser.email)!!
                )
            }

        return GroupResultView(
            id = table.id.toString(),
            name = table.name,
            authorities = table.linkedAuthorities.map { it.id },
            users = userList
        )
    }

    override fun inputToTable(table: Optional<GroupEntity>, input: GroupInsertView): GroupEntity {
        val qAuth = QAuthorityEntity.authorityEntity
        val authorities = if (input.authorities.isNotEmpty()) {
            val authorities = jpaQuery.selectFrom(qAuth).where(qAuth.id.`in`(input.authorities)).fetch()
            val unknownAuthorities = input.authorities.toMutableSet()
            unknownAuthorities.removeAll(authorities.map { it.id })
            if (unknownAuthorities.isNotEmpty()) {
                throw BadRequestException("权限名：$unknownAuthorities 不是已知的权限名称")
            }
            authorities
        } else {
            listOf()
        }
        return table.orElse(GroupEntity(id = idGenerate.nextId(), name = input.name)).apply {
            name = input.name
            linkedAuthorities.clear()
            linkedAuthorities.addAll(authorities)
        }
    }

    override fun afterWriteHook(table: GroupEntity) {
        table.users.forEach { authorityCache.clear(it.id.toString()) }
    }
}
