package i.watch.modules.log.model.db

import i.watch.modules.client.model.db.ClientEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "t_log_client_online")
class ClientOnlineEntity(

    @Id
    @Column(name = "t_id", nullable = false)
    val id: Long = 0,
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    val linkedClient: ClientEntity,
    @Column(name = "push_date", nullable = false)
    val date: LocalDateTime,
    @OneToOne
    val status: ClientStatusEntity
)
