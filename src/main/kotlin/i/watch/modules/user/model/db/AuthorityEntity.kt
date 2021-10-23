package i.watch.modules.user.model.db

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

/**
 *
 * 权限表
 *
 * @property id Int 权限 ID
 * @property name String 权限名称
 * @property details String 权限详情
 * @constructor
 */
@Entity
@Table(name = "t_authority")
class AuthorityEntity(
    @Id
    @Column(name = "t_id", nullable = false)
    val id: String,
    @Column(nullable = false)
    val details: String,
    @ManyToMany(mappedBy = "linkedAuthorities")
    val groups: MutableSet<GroupEntity> = mutableSetOf()
)
