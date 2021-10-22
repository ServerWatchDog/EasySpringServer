package i.watch.hooks.security

import i.watch.hooks.security.auth.ISession
import i.watch.hooks.security.auth.ISessionService
import i.watch.hooks.security.auth.Session
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class SessionInfoResolver(private val sessionService: ISessionService<*>) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(Session::class.java) &&
            ISession::class == parameter.parameterType.kotlin
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        return sessionService.getSessionByToken {
            webRequest.getHeader(it)
                ?: throw RuntimeException("此处不应该为 NULL")
        }
    }
}
