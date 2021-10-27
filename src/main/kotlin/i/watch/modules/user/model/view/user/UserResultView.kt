package i.watch.modules.user.model.view.user

data class UserResultView(
    val id: String,
    val name: String,
    val email: String,
    val password: String = "******",
    val registerDate: String,
    val groupName: String
)
