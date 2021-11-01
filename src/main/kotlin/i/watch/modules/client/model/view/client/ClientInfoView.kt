package i.watch.modules.client.model.view.client

data class ClientInfoView(
    val arch: String,
    val system: String,
    val cpuName: String,
    val cpuCore: Int,
    val memory: Long,
    val disk: Long,
)
