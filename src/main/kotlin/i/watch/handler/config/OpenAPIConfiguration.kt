package i.watch.handler.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

/**
 * SpringDoc OpenAPI 配置
 */
@Component
class OpenAPIConfiguration {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .components(
                Components().addSecuritySchemes(
                    "bearer",
                    SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                )
            )
            .info(
                Info().title("WatchDog API").version("last").description(
                    """
                       WatchDog 服务器后端 API.
                    """.trimIndent()
                )
                    .termsOfService("https://github.com/orgs/ServerWatchDog/")
                    .license(
                        License().name("GNU Affero General Public License v3.0")
                    )
            )
    }
}
