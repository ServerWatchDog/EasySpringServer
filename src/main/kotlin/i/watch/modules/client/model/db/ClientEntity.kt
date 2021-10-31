package i.watch.modules.client.model.db

import i.watch.modules.user.model.db.UserEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(
    name = "t_client",
    indexes = [
        Index(columnList = "token"),
    ]
)
class ClientEntity(
    @Id
    @Column(name = "t_id", nullable = false)
    val id: Long = 0,
    @Column(name = "name", length = 128, nullable = false)
    var name: String,
    @Column(name = "token", length = 512, nullable = false)
    var token: String,
    @Column(name = "enabled", nullable = false)
    var enable: Boolean,
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    var linkedUser: UserEntity,
) {
    @ManyToMany(mappedBy = "linkedClients")
    val groups: MutableSet<ClientGroupEntity> = mutableSetOf()
}
