package i.watch.handler.security.encrypt

data class EncryptView(
    val type: EncryptType,
    val cipher: String
) {
    enum class EncryptType {
        RSA
    }
}
