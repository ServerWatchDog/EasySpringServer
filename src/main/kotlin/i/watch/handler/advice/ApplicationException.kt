package i.watch.handler.advice

import org.springframework.http.HttpStatus

/**
 *  业务级错误抛出
 */
open class ApplicationException(val code: HttpStatus, val errorMessage: String) : RuntimeException(errorMessage)
