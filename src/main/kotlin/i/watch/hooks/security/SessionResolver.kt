package i.watch.hooks.security

import i.watch.hooks.error.ForbiddenException
import i.watch.hooks.security.auth.CachedBeanLoader
import i.watch.hooks.security.auth.ISession
import i.watch.hooks.security.auth.ISessionService
import i.watch.hooks.security.auth.Permission
import i.watch.hooks.security.auth.Session
import i.watch.utils.TokenUtils
import i.watch.utils.getLogger
import org.springframework.core.MethodParameter
import org.springframework.core.annotation.AnnotatedElementUtils
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
class SessionResolver(
    private val cachedContext: CachedBeanLoader
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
        val tokenType = TokenUtils.getTokenHeader(token).trim()
        val permission =
            AnnotatedElementUtils
                .findMergedAnnotation(parameter.annotatedElement, Permission::class.java)
                ?: throw RuntimeException("此处不应该为 NULL")
        if (tokenType != permission.tag) {
            throw ForbiddenException("Token 不适用此接口.")
        }
        val sessionService = try {
            cachedContext
                .getBean("AUTH:${permission.tag}", ISessionService::class)
        } catch (e: Exception) {
            logger.debug("Token {} 无法被识别.", token)
            throw ForbiddenException("Token 校验失败.")
        }
        return sessionService.getSessionByToken(
            token
        )
    }
}
