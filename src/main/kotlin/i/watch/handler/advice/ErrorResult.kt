package i.watch.handler.advice

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.HttpStatus

/**
 * 错误信息包装类
 * @property code Int
 * @property message String
 * @constructor
 */
data class ErrorResult(
    @JsonIgnore
    val httpCode: HttpStatus,
    val code: Int = httpCode.value(),
    val message: String
)
