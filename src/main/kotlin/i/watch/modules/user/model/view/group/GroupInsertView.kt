package i.watch.modules.user.model.view.group

data class GroupInsertView(
    val name: String,
    val authorities: Set<String>
)
