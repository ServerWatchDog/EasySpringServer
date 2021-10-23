package i.watch.hooks.security

import i.watch.hooks.error.ForbiddenException
import i.watch.hooks.security.auth.ISessionService
import i.watch.hooks.security.auth.Permission
import i.watch.utils.TokenUtils
import i.watch.utils.getLogger
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 权限拦截器
 */
@Configuration
class AuthorityInterceptor(private val applicationContext: ApplicationContext) : HandlerInterceptor {
    val logger = getLogger()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        if (handler.hasMethodAnnotation(Permission::class.java)
        ) {
            val token = TokenUtils.decodeToken { header ->
                request.getHeader(header) ?: throw ForbiddenException("需要 Token.")
            }
            val sessionService = try {
                applicationContext
                    .getBean("AUTH:${TokenUtils.getTokenHeader(token)}", ISessionService::class.java)
            } catch (e: Exception) {
                logger.debug("Token {} 无法被识别.", token)
                throw ForbiddenException("Token 校验失败.")
            }
            val session = try {
                sessionService.getSessionByToken(token)
            } catch (e: NullPointerException) {
                logger.debug("Token {} 未找到.", token, e)
                throw ForbiddenException("Token 校验失败.")
            }

            session.refresh()
            val permission = handler.getMethodAnnotation(Permission::class.java) ?: return true
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
        }
        return true
    }
}
