package i.watch.handler.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "global.server")
data class SoftConfigProperties(
    var api: String,
    var name: String,
    var host: String,
    var dev: Boolean
)
