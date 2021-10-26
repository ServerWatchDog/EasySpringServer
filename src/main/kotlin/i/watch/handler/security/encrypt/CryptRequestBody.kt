package i.watch.handler.security.encrypt

import io.swagger.v3.oas.annotations.Parameter

/**
 * 对数据解密
 */
@Parameter(
    description = "加密的HTTP请求",
    required = true
)
annotation class CryptRequestBody
