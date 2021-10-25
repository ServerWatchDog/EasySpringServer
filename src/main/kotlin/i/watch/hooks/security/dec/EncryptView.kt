package i.watch.hooks.security.dec

data class EncryptView(
    val type: EncryptType,
    val cipher: String
) {
    enum class EncryptType {
        RSA
    }
}
