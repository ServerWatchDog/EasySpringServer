package i.watch.hooks.security.auth

interface ISessionService<T : ISession> {
    fun getSessionByToken(func: (String) -> String): T
    fun verify(session: T, permissions: Array<String>): Boolean

    @Suppress("UNCHECKED_CAST")
    fun verify0(session: ISession, permissions: Array<String>): Boolean {
        return verify(session as T, permissions)
    }
}
