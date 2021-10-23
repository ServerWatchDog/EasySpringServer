package i.watch.hooks.security.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement

/**
 * 拦截请求注入，标记此注解则表明路径受到权限管理器拦截
 */
@Operation(
    security = [SecurityRequirement(name = "bearer")]
)
annotation class Permission(
    /**
     * 指定权限，如果未指定权限，则表明此接口只要求登录就行
     */
    val permissions: Array<String> = []
)
