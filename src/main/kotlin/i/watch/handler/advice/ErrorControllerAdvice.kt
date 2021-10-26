package i.watch.handler.advice

import i.watch.utils.getLogger
import org.springframework.http.HttpStatus
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 错误包装
 */
@ControllerAdvice
@RestControllerAdvice
class ErrorControllerAdvice {

    private val logger = getLogger()

    @ExceptionHandler(ApplicationException::class)
    fun applicationAdvice(error: ApplicationException): ErrorResult {
        if (error.code.is5xxServerError) {
            logger.error("发生未知错误.", error)
        } else {
            logger.debug("发生错误.", error)
        }
        return ErrorResult(error.code, message = error.errorMessage)
    }

    @ExceptionHandler(Exception::class)
    fun exceptionAdvice(error: Exception): ErrorResult {
        logger.error("服务器内部错误.", error)
        return ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, message = "INTERNAL_SERVER_ERROR")
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun mediaTypeNotSupportedExceptionAdvice(error: HttpMediaTypeNotSupportedException): ErrorResult {
        logger.error("请求格式错误.", error)
        return ErrorResult(HttpStatus.BAD_REQUEST, message = "请求体不合法.")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun exceptionAdvice(error: MethodArgumentNotValidException): ErrorResult {
        logger.debug("格式化异常.", error)
        return ErrorResult(
            HttpStatus.BAD_REQUEST,
            message = error.fieldErrors.joinToString {
                "字段${it.field}${it.defaultMessage ?: ""}"
            }
        )
    }
}
