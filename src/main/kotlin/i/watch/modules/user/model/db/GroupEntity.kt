package i.watch.modules.user.model.db

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "t_group")
class GroupEntity(
    @Id
    @Column(name = "t_id", nullable = false)
    val id: Long = 0,
    @Column(name = "name", length = 128, nullable = false)
    val name: String,
) {
    @OneToMany(mappedBy = "linkGroup")
    var users: MutableSet<UserEntity> = mutableSetOf()

    @ManyToMany
    @JoinTable(
        name = "t_group_to_authority",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id")]
    )
    var linkedAuthorities: MutableSet<AuthorityEntity> = mutableSetOf()
}
