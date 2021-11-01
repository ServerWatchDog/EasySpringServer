package i.watch.modules.log.model.db

import i.watch.modules.client.model.db.ClientEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "t_log_client_info")
class ClientInfoEntity(
    @Id
    @Column(name = "t_id", nullable = false)
    val id: Long = 0,
    @Column(name = "arch", nullable = false)
    val arch: String,
    @Column(name = "os_name", nullable = false)
    val system: String,
    @Column(name = "cpu_name", nullable = false)
    val cpuName: String,
    @Column(name = "cpu_core", nullable = false)
    val cpuCore: Int,
    @Column(name = "memory_size", nullable = false)
    val memory: Long,
    @Column(name = "disk_size", nullable = false)
    val disk: Long,
    @Column(name = "create_time", nullable = false)
    val createDate: LocalDateTime,
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    val client: ClientEntity
) {
    @OneToMany(mappedBy = "linkedInfo")
    val statuses: MutableSet<ClientStatusEntity> = mutableSetOf()
}
