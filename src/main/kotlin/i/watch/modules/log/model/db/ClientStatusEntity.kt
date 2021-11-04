package i.watch.modules.log.model.db

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "t_log_client_online_status")
class ClientStatusEntity(

    @Id
    @Column(name = "t_id", nullable = false)
    val id: Long = 0,
    @OneToOne
    val onlineEntity: ClientOnlineEntity,
    val cpuStage: Long,
    val usedMemory: Long,
    val usedDisk: Long,
    val usedNetwork: Long,
    val putDate: LocalDateTime
)
