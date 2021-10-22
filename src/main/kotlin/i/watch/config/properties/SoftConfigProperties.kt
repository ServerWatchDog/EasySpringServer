package i.watch.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "global.server")
data class SoftConfigProperties(
    var api: String,
    var name: String,
    var host: String,
    var path: String,
    var sessionTimeout: Long ,
    var disableCors: Boolean,
)
