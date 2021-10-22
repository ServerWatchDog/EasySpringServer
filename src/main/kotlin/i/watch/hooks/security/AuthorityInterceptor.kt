package i.watch.hooks.security

import i.watch.hooks.error.ForbiddenException
import i.watch.hooks.security.auth.ISessionService
import i.watch.hooks.security.auth.Permission
import i.watch.utils.getLogger
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 权限拦截器
 */
@Configuration
class AuthorityInterceptor : HandlerInterceptor {

    @Resource
    private lateinit var sessionService: ISessionService<*>

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }
        if (handler.hasMethodAnnotation(Permission::class.java)
        ) {
            val session = try {
                sessionService.getSessionByToken { header ->
                    request.getHeader(header) ?: throw ForbiddenException("路径需要会话支持.")
                }
            } catch (e: NullPointerException) {
                throw ForbiddenException("会话已过期.")
            }

            session.refresh()
            val permission = handler.getMethodAnnotation(Permission::class.java) ?: return true
            if (permission.permissions.isEmpty()) {
                return true
            }
            if (sessionService.verify0(session, permission.permissions).not()) {
                getLogger().info("权限问题.")
                throw ForbiddenException("没有权限！")
            }
        }
        return true
    }
}
