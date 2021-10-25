package i.watch.modules.installer.service

import i.watch.hooks.security.auth.ISessionService.GenericISessionService
import i.watch.utils.RedisUtils
import i.watch.utils.TokenUtils
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service("AUTH:inst")
class InstallSessionService(
    private val redisUtils: RedisUtils
) : GenericISessionService<InstallerSession> {

    fun initToken() {
        val token = TokenUtils.randomToken("inst")
        redisUtils.initMap(key = "installer::$token").expire(1, TimeUnit.DAYS)
        println(
            """
                    ==============================================================

                    安装密钥: $token 
                    
                    注意，此密钥仅在 1 天内有效
                    
                    ==============================================================
            """.trimIndent()
        )
    }

    override fun getSession(token: String): InstallerSession {
        return InstallerSession(redisUtils.withMap("installer::$token"))
    }

    override fun verifySession(session: InstallerSession, permissions: Array<String>): Boolean {
        println(session.createTime)
        return true
    }
}
