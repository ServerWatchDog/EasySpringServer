package i.watch.modules.dev.controller

import i.watch.handler.inject.encrypt.EncryptView
import i.watch.modules.info.service.IInfoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.ContentCachingRequestWrapper
import javax.servlet.http.HttpServletResponse

@Tag(name = "加密测试", description = "在测试环境下对数据进行加、解密")
@ConditionalOnProperty(value = ["global.server.dev"], havingValue = "true")
@RestController
@RequestMapping("/dev/crypt")
class EncryptController(
    private val infoService: IInfoService
) {
    enum class Type {
        Public,
        Private
    }

    @PostMapping("/decode/{mode}")
    fun decode(
        @PathVariable("mode") mode: Type,
        @RequestBody encryptView: EncryptView,
        response: HttpServletResponse
    ) {
        when (mode) {
            Type.Public -> {
                infoService.getPublicKey(encryptView.type)
            }
            Type.Private -> {
                infoService.getPrivateKey(encryptView.type)
            }
        }.decodeText(encryptView.cipher).run { response.writer.println(this) }
    }

    @Operation(
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content =
            [Content(schema = Schema(implementation = String::class))]
        )
    )
    @PostMapping("/encode/{mode}/{type}")
    fun encode(
        @PathVariable("mode") mode: Type,
        @PathVariable("type") type: EncryptView.EncryptType,
        request: ContentCachingRequestWrapper
    ): EncryptView {
        if (request.contentAsByteArray.isEmpty()) {
            request.inputStream.readAllBytes()
        }
        val data = request.contentAsByteArray.toString(Charsets.UTF_8)
        return when (mode) {
            Type.Public -> {
                infoService.getPublicKey(type)
            }
            Type.Private -> {
                infoService.getPrivateKey(type)
            }
        }.encodeText(data).run { EncryptView(type, this) }
    }
}
