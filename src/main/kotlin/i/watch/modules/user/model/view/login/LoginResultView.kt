package i.watch.modules.user.model.view.login

data class LoginResultView(
    val status: LoginResultType,
    val token: String = ""
) {
    enum class LoginResultType {
        SUCCESS,
        NEED_2FA,
        NEED_EMAIL,
        NEED_SMS,
    }
}
