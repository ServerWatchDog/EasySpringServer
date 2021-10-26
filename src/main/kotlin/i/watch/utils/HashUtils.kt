package i.watch.utils

import com.github.open_edgn.security4k.hash.crc32Sum
import com.github.open_edgn.security4k.hash.sha256Sum
import com.github.open_edgn.security4k.hash.sha384Sum
import com.github.open_edgn.security4k.hash.sha512Sum

/**
 * 安全配置
 */
class HashUtils(
    private val defaultAlgorithm: HashMode = HashMode.S_256_3,
    private val saltCode: String
) {
    /**
     * 明文校验
     *
     * @param plain String 明文
     * @param hash String 哈希
     * @return Boolean
     */
    fun verify(plain: String, hash: String): Boolean {
        val data = hash.split(Regex("="), 3)
        if (data.size != 3) {
            throw RuntimeException("此加密类型不被识别. 详细内容：$hash")
        }
        if (data[1] != saltCode.crc32Sum()) {
            throw RuntimeException("Salt不匹配.")
        }
        return create(HashMode.valueOf(data[0]), plain) == hash
    }

    fun create(plain: String): String = create(defaultAlgorithm, plain)

    /**
     * 创建哈希
     *
     * TYPE=CRC32(salt)=HASH
     *
     * @param algorithm HashMode 哈希算法
     * @param plain String 明文
     * @return String 哈希
     */
    fun create(algorithm: HashMode, plain: String): String {
        return "${algorithm.name}=${saltCode.crc32Sum()}=${algorithm.generate.hash(plain, saltCode)}"
    }

    enum class HashMode(val generate: HashGenerate) {
        S_256_3(object : HashGenerate {
            override fun hash(plain: String, salt: String): String {
                // sha256 三次取哈希
                return "$salt-$plain".sha256Sum()
                    .run { "$salt-$this".sha256Sum() }
                    .run { "$salt-$this".sha256Sum() }
            }
        }),
        S_384_3(object : HashGenerate {
            override fun hash(plain: String, salt: String): String {
                // sha384 三次取哈希
                return "$salt-$plain".sha384Sum()
                    .run { "$salt-$this".sha384Sum() }
                    .run { "$salt-$this".sha384Sum() }
            }
        }),
        S_512_3(object : HashGenerate {
            override fun hash(plain: String, salt: String): String {
                // sha512 三次取哈希
                return "$salt-$plain".sha512Sum()
                    .run { "$salt-$this".sha512Sum() }
                    .run { "$salt-$this".sha512Sum() }
            }
        }),
    }

    interface HashGenerate {
        fun hash(plain: String, salt: String): String
    }
}
