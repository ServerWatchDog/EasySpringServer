package i.watch.modules.user.model.view.user

import javax.validation.constraints.Email

data class UserInsertView(
    val name: String,
    @Email
    val email: String,
    val password: String,
    val group: String
)
