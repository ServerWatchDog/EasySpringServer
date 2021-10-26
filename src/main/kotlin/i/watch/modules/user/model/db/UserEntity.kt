package i.watch.modules.user.model.db

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.Email

@Entity
@Table(
    name = "t_user",
    indexes = [
        Index(columnList = "email"),
        Index(columnList = "name")
    ]
)
class UserEntity(
    @Id
    @Column(name = "t_id", nullable = false)
    val id: Long = 0,
    @Column(name = "email", length = 128, nullable = false)
    @Email
    var email: String,
    @Column(name = "name", length = 128, nullable = false)
    var name: String,
    @Column(name = "password", length = 128, nullable = false)
    var password: String,
    @Column(name = "twoFactor", length = 128, nullable = false)
    var twoFactor: String,
    @Column(name = "registerTime", nullable = false)
    val registerTime: LocalDateTime = LocalDateTime.now(),
    @ManyToOne
    @JoinColumn(name = "groupId", nullable = false)
    val linkGroup: GroupEntity
)
