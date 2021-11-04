package i.watch.modules.client.model.view.online

data class OnlineItemView(
    val name: String,
    val arch: String,
    val system: String,
    val cpuName: String,
    val cpuStage: Long,
    val memory: Long,
    val usedMemory: Long,
    val disk: Long,
    val usedDisk: Long,
    val usedNetwork: Long,
)
