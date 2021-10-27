package i.watch.modules.user.model.view.register

data class RegisterResultView(
    val status: RegisterStatus
) {
    enum class RegisterStatus {
        SUCCESS,
        SEND_EMAIL
    }
}
