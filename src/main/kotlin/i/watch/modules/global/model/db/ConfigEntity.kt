package i.watch.modules.global.model.db

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "t_config")
data class ConfigEntity(
    @Id
    @Column(name = "t_key", nullable = false)
    val key: String,
    @Column(name = "value", nullable = false)
    val value: String
)
