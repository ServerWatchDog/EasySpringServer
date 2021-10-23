package i.watch.modules.user.model.view

data class RegisterResultView(
    val status: RegisterStatus
) {
    enum class RegisterStatus {
        SUCCESS,
        SEND_EMAIL
    }
}
