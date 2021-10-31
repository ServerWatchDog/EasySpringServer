package i.watch.modules.client.model.db

import i.watch.modules.user.model.db.UserEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "t_client_group")
class ClientGroupEntity(
    @Id
    @Column(name = "t_id", nullable = false)
    val id: Long = 0,
    @Column(name = "name", length = 128, nullable = false)
    var name: String,
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    val linkedUser: UserEntity,
) {
    @ManyToMany
    @JoinTable(
        name = "t_client_group_to_client",
        joinColumns = [JoinColumn(name = "client_group_id")],
        inverseJoinColumns = [JoinColumn(name = "client_id")]
    )
    var linkedClients: MutableSet<ClientEntity> = mutableSetOf()
}
