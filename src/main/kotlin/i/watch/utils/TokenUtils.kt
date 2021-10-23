package i.watch.utils

import java.util.UUID
import kotlin.experimental.xor

object TokenUtils {
    private const val sessionHeader = "Authorization"

    fun decodeToken(func: (String) -> String): String {
        return func(sessionHeader).replace("Bearer ", "")
    }

    /**
     * 生成随机等长 Token，非标准UUID ！
     * @param name String 最大长度为5且只支持 ISO_8859_1 字符
     * @return String
     */
    fun randomToken(name: String): String {
        val header = String.format("%-6s", name).substring(0, 5)
        val session = UUID.randomUUID()
        val seed = (session.leastSignificantBits % Byte.MAX_VALUE).toByte()
        val head = header.toByteArray(Charsets.ISO_8859_1).joinToString(separator = "") {
            String.format("%02x", it xor seed)
        }
        return "$head-$session"
    }

    fun getTokenHeader(token: String): String {
        val split = token.split(Regex("-"), 2)
        if (split.size != 2) {
            throw IllegalArgumentException("无法解析 Token")
        }
        val uuid = UUID.fromString(split[1])
        val seed = (uuid.leastSignificantBits % Byte.MAX_VALUE).toByte()
        val head = split.first()
        if (head.length % 2 != 0) {
            throw IllegalArgumentException("无法解析 Token")
        }
        return head.windowed(2, 2).map { (it.toInt(16).toByte() xor seed) }.toByteArray().toString(Charsets.ISO_8859_1)
    }
}
