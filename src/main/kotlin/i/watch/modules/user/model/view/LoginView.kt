package i.watch.modules.user.model.view

data class LoginView(
    val account: String,
    val password: String,
    val code: String = ""
)
