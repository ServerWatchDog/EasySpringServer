package i.watch.config

import i.watch.config.properties.SoftConfigProperties
import i.watch.hooks.security.AuthorityInterceptor
import i.watch.hooks.security.SessionInfoResolver
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration(
    private val authorityInterceptor: AuthorityInterceptor,
    private val sessionInfoResolver: SessionInfoResolver,
    private val softConfigProperties: SoftConfigProperties
) : WebMvcConfigurer {

    @Value("\${global.server.api}")
    private lateinit var apiPath: String

    @Value("\${springdoc.api-docs.path}")
    private lateinit var apiDocPath: String

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authorityInterceptor)
            .addPathPatterns("$apiPath/**")
            .excludePathPatterns("$apiDocPath/**")
        // 拦截 API 接口
        super.addInterceptors(registry)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(sessionInfoResolver)
        super.addArgumentResolvers(resolvers)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        if (softConfigProperties.dev) {
            registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("*")
        }
        super.addCorsMappings(registry)
    }
}
