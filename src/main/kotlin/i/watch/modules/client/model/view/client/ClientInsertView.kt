package i.watch.modules.client.model.view.client

data class ClientInsertView(
    val name: String,
    val linkedUser: Long,
    val enabled: Boolean,
    val linkedGroup: Set<Long>,
    val refreshToken: Boolean
)
