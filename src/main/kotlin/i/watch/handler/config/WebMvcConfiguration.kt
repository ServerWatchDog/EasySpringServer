package i.watch.handler.config

import i.watch.handler.config.properties.SoftConfigProperties
import i.watch.handler.filter.RequestJsonFilter
import i.watch.handler.inject.encrypt.EncryptResolver
import i.watch.handler.inject.page.RestPageResolver
import i.watch.handler.inject.session.AuthorityInterceptor
import i.watch.handler.inject.session.SessionResolver
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration(
    private val authorityInterceptor: AuthorityInterceptor,
    private val sessionResolver: SessionResolver,
    private val encryptResolver: EncryptResolver,
    private val restPageResolver: RestPageResolver,
    private val jsonFilter: RequestJsonFilter,
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
        resolvers.add(sessionResolver)
        resolvers.add(encryptResolver)
        resolvers.add(restPageResolver)
        super.addArgumentResolvers(resolvers)
    }

    @Bean
    fun jsonFilterRegister(): FilterRegistrationBean<*> {
        return FilterRegistrationBean<RequestJsonFilter>().apply {
            filter = jsonFilter
            addUrlPatterns("/*")
            setName("requestJsonFilter")
            isEnabled = true
            order = 1
        }
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
