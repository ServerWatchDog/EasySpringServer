package i.watch.hooks.security.auth

/**
 * 会话接口
 *
 * 对不同接口需要不同的会话做解耦
 */
interface ISessionService {
    /**
     * 根据 token 获取会话信息
     */
    fun getSessionByToken(token: String): ISession

    /**
     * 验证此会话是否有权限访问
     */
    fun verify(session: ISession, permissions: Array<String>): Boolean
}
