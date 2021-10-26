package i.watch.modules.user.model.db

/**
 *
 * 权限
 *
 *
 * @property details String 权限解释
 * @constructor
 */
enum class Authority(
    val details: String,
) {
    CONSOLE_LOGIN("控制台登录权限"),
}

enum class AuthorityGroup(vararg authority: Authority) {
    ADMIN(Authority.CONSOLE_LOGIN)
}
