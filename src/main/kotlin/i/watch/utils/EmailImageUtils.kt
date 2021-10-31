package i.watch.utils

import com.github.open_edgn.security4k.hash.md5Sum

object EmailImageUtils {
    fun getImageUrl(email: String): String {
        if (email.endsWith("@qq.com") && email.replace("@qq.com", "")
            .contains(Regex("[0-9]*"))
        ) {
            return "https://q1.qlogo.cn/g?b=qq&nk=${email.replace("@qq.com", "")}&s=100"
        }
        return "https://www.gravatar.com/avatar/${email.lowercase().md5Sum().lowercase()}?s=200"
    }
}
