package i.watch.modules.info.controller

import i.watch.modules.info.service.IInfoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${global.server.api}/info")
class InfoController(private val softInfoService: IInfoService) {

    @GetMapping("/encrypt")
    fun publicKey() = softInfoService.encryptInfo()
}
