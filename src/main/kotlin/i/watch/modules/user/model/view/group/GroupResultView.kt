package i.watch.modules.user.model.view.group

import i.watch.utils.template.crud.CRUDOutputView

data class GroupResultView(
    override val id: String,
    val name: String,
    val authorities: List<String>,
    val users: List<GroupUserResultView>

) : CRUDOutputView {
    data class GroupUserResultView(
        val id: String,
        val name: String,
        val email: String,
    )
}
