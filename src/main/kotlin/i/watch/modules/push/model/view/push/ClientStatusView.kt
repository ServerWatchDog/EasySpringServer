package i.watch.modules.push.model.view.push

data class ClientStatusView(
    val cpuStage: Long,
    val usedMemory: Long,
    val usedDisk: Long,
    val usedNetwork: Long
)
