package i.watch.handler.config.properties

import i.watch.handler.inject.encrypt.EncryptView
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "global.server.encrypt")
data class EncryptProperties(
    var type: EncryptView.EncryptType,
    var publicKey: String,
    var privateKey: String
)
