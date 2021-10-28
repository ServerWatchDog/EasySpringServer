package i.watch.handler.inject.session

import i.watch.handler.advice.ForbiddenException
import i.watch.utils.TokenUtils
import i.watch.utils.getLogger
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 权限拦截器
 */
@Configuration
class AuthorityInterceptor(private val cachedContext: CachedBeanLoader) : HandlerInterceptor {
    val logger = getLogger()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        // 通过查询注解来获取权限
        val permission = handler.getMethodAnnotation(Permission::class.java)
            ?: AnnotationUtils.findAnnotation(handler.bean.javaClass, Permission::class.java) ?: return kotlin.run {
            logger.debug("放行路径 {}.", request.pathInfo)
            true
        }

        val token = TokenUtils.decodeToken { header ->
            request.getHeader(header) ?: throw ForbiddenException("需要 Token.")
        }
        val sessionService = try {
            val tokenType = TokenUtils.getTokenHeader(token).trim()
            if (tokenType != permission.tag) {
                throw ForbiddenException("Token 不适用此接口.")
            }
            cachedContext.getBean("AUTH:${permission.tag}", ISessionService::class)
        } catch (e: ForbiddenException) {
            throw e
        } catch (e: Exception) {
            logger.debug("Token {} 无法被识别.", token)
            throw ForbiddenException("Token 错误.")
        }
        val session = try {
            sessionService.getSessionByToken(token)
        } catch (e: NullPointerException) {
            logger.debug("Token {} 未找到.", token, e)
            throw ForbiddenException("Token 已过期.")
        }

        session.refresh()
        if (permission.permissions.isEmpty()) {
            return true
        }
        if (sessionService.verify(session, permission.permissions).not()) {
            logger.debug(
                "权限校验问题. 路径：{} 需要 {} 权限.",
                request.contextPath, permission.permissions
            )
            throw ForbiddenException("没有权限！")
        }
        return true
    }
}
