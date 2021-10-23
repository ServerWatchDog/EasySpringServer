package i.watch.modules.user.model.view

import javax.validation.constraints.Email

data class RegisterView(
    @field:Email
    val email: String,
    val name: String,
    val password: String,
    val verifyCode: String
)
