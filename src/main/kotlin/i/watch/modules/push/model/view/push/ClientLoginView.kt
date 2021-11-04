package i.watch.modules.push.model.view.push

import java.time.LocalDateTime

data class ClientLoginView(
    val arch: String,
    val system: String,
    val cpuName: String,
    val cpuCore: Int,
    val memory: Long,
    val disk: Long,
    val createDate: LocalDateTime,

)
