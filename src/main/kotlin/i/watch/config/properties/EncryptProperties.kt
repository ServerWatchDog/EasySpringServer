package i.watch.config.properties

import i.watch.hooks.security.dec.EncryptView
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "global.server.encrypt")
data class EncryptProperties(
    val type: EncryptView.EncryptType,
    val publicKey: String,
    val privateKey: String
)
