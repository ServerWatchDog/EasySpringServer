package i.watch.modules.user.model.db

/**
 *
 * 权限
 *
 *
 * @property details String 权限解释
 * @property depends Array<Authority> 权限依赖 （启用此权限则表明依赖权限会生效）
 * @constructor
 */
enum class Authority(
    val details: String,
) {
    CONSOLE_LOGIN("控制台登录权限"),
    NODE_LOGIN("控制台登录权限"),
}
