package i.watch.handler.advice

import org.springframework.http.HttpStatus

class ForbiddenException(message: String = "FORBIDDEN") : ApplicationException(HttpStatus.FORBIDDEN, message)
class NotFoundException(message: String = "NOT FOUND") : ApplicationException(HttpStatus.NOT_FOUND, message)
class BadRequestException(message: String = "Bad Request") : ApplicationException(HttpStatus.BAD_REQUEST, message)

class InternalServerException(message: String = "服务器内部错误！") :
    ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, message)
