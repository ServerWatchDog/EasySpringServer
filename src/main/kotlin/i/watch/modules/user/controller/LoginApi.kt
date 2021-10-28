package i.watch.modules.user.controller

import i.watch.handler.inject.encrypt.CryptRequestBody
import i.watch.modules.user.model.view.login.LoginResultView
import i.watch.modules.user.model.view.login.LoginView
import i.watch.modules.user.model.view.register.RegisterResultView
import i.watch.modules.user.model.view.register.RegisterView
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Tag(name = "用户登录/注册", description = "使用此 API 进行登录、注册")
@RestController
@RequestMapping("\${global.server.api}/view/user")
interface LoginApi {
    @Operation(
        summary = "用户登录",
        description = "控制台用户登录"
    )
    @PostMapping(
        "/login",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun login(
        @Valid @CryptRequestBody loginView: LoginView
    ): LoginResultView

    @PostMapping(
        "/register",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun register(
        @Valid @CryptRequestBody registerView: RegisterView
    ): RegisterResultView
}
