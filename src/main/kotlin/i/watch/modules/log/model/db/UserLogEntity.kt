package i.watch.modules.log.model.db

import i.watch.modules.user.model.db.UserEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "t_log_user")
class UserLogEntity(
    @Id
    @Column(name = "t_id", nullable = false)
    val id: Long = 0,
    @ManyToOne
    val linkedUser: UserEntity,
    val message: String,
    val date: LocalDateTime,
) {
    @ManyToMany
    @JoinTable(
        name = "t_log_user_to_tag",
        joinColumns = [JoinColumn(name = "log_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags: MutableSet<LogTagEntity> = mutableSetOf()
}
