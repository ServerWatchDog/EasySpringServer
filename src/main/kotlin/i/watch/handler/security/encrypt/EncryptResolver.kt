package i.watch.handler.security.encrypt

import com.fasterxml.jackson.databind.ObjectMapper
import i.watch.handler.advice.BadRequestException
import i.watch.modules.info.service.IInfoService
import i.watch.utils.getLogger
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.util.ContentCachingRequestWrapper
import javax.annotation.Resource

/**
 * 会话信息注入
 */
@Component
class EncryptResolver(
    private val softConfigService: IInfoService
) : HandlerMethodArgumentResolver {
    @Resource(name = "redisJacksonManager")
    private lateinit var objectManager: ObjectMapper

    private val logger = getLogger()

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CryptRequestBody::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val nativeRequest = webRequest.getNativeRequest(ContentCachingRequestWrapper::class.java)
            ?: throw BadRequestException("格式不合法！") // TODO: 抛出类型可能错误
        return try {
            val data = nativeRequest.contentAsByteArray.toString(Charsets.UTF_8)
            val encryptView = objectManager.readValue(data, EncryptView::class.java)
            val privateKey = softConfigService.getPrivateKey(encryptView.type)
            val decodeText = privateKey.decodeText(encryptView.cipher)
            objectManager.readValue(decodeText, parameter.parameter.type)
        } catch (e: Exception) {
            logger.debug("解析错误！", e)
            throw BadRequestException("格式不合法！")
        }
    }
}
