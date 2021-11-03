package i.watch.modules.push.model.view.online

data class OnlineResultView(
    val data: List<OnlineItemView>
)

data class OnlineItemView(
    val name: String,
    val arch: String,
    val system: String,
    val cpuName: String,
    val cpuStage: Long,
    val memory: Long,
    val userMemory: Long,
    val disk: Long,
    val usedDisk: Long,
    val usedNetwork: Long,
)
