package i.watch.modules.log.model.db

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "t_log_tag")
class LogTagEntity(
    @Id
    @Column(name = "t_id", nullable = false)
    val id: String
)
