package i.watch.modules.user.model.view.user

import i.watch.utils.template.crud.CRUDOutputView

data class UserResultView(
    override val id: String,
    val name: String,
    val email: String,
    val password: String = "******",
    val registerDate: String,
    val groupName: String
) : CRUDOutputView
