package i.watch.modules.client.model.view.client

import i.watch.utils.template.crud.CRUDOutputView

data class ClientResultView(
    override val id: String,
    val user: SimpleUserResultView,
    val groups: List<SimpleClientGroupResultView>,
    val enabled: Boolean,
    val token: String,
) : CRUDOutputView {
    data class SimpleUserResultView(
        val id: String,
        val name: String,
        val email: String
    )

    data class SimpleClientGroupResultView(
        val id: Long,
        val name: String
    )
}
