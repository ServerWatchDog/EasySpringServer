package i.watch.modules.user.service

interface IUserSessionService {
    fun createSessionByUserId(id: Long): String
}
