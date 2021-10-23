package i.watch.hooks.security

import i.watch.hooks.error.ForbiddenException
import i.watch.hooks.security.auth.ISession
import i.watch.hooks.security.auth.ISessionService
import i.watch.hooks.security.auth.Session
import i.watch.utils.TokenUtils
import i.watch.utils.getLogger
import org.springframework.context.ApplicationContext
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import kotlin.reflect.full.isSubclassOf

/**
 * 会话信息注入
 */
@Component
class SessionInfoResolver(
    private val applicationContext: ApplicationContext
) : HandlerMethodArgumentResolver {
    private val logger = getLogger()
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(Session::class.java) &&
            parameter.parameterType.kotlin.isSubclassOf(ISession::class)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val token = TokenUtils.decodeToken {
            webRequest.getHeader(it)
                ?: throw RuntimeException("此处不应该为 NULL")
        }
        val sessionService = try {
            applicationContext
                .getBean("AUTH:${TokenUtils.getTokenHeader(token)}", ISessionService::class.java)
        } catch (e: Exception) {
            logger.debug("Token {} 无法被识别.", token)
            throw ForbiddenException("Token 校验失败.")
        }
        return sessionService.getSessionByToken(
            token
        )
    }
}
