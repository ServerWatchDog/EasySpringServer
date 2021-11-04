package i.watch.modules.push.model.view.push

data class ClientPushView(
    val cpuStage: Long,
    val usedMemory: Long,
    val usedDisk: Long,
    val usedNetwork: Long,
)
