package i.watch.modules.global.controller

import i.watch.modules.global.service.ISoftInfoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${global.server.api}/info")
class SoftInfoController(private val softInfoService: ISoftInfoService) {

    @GetMapping("/encrypt")
    fun pubkey() = softInfoService.encryptInfo()
}
