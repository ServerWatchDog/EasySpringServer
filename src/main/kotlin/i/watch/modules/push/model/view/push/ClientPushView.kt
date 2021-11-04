package i.watch.modules.push.model.view.push

import java.time.LocalDateTime

data class ClientPushView(
    val cpuStage: Long,
    val usedMemory: Long,
    val usedDisk: Long,
    val usedNetwork: Long,
    val putDate: LocalDateTime
)
