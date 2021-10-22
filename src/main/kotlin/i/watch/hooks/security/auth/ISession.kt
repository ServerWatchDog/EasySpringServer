package i.watch.hooks.security.auth

/**
 * 会话接口
 */
interface ISession {
    /**
     * 刷新事件
     */
    fun refresh()
}
