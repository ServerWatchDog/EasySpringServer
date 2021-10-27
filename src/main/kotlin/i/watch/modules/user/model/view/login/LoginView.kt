package i.watch.modules.user.model.view.login

import javax.validation.constraints.Email

data class LoginView(
    @field:Email
    val account: String,
    val password: String,
    val code: String = ""
)
