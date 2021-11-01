package i.watch.modules.info.controller

import i.watch.modules.info.service.IInfoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${global.server.api}/info")
class InfoController(private val softInfoService: IInfoService) {

    @GetMapping("")
    fun getInfo() = softInfoService.getInfo()

    @GetMapping("authority")
    fun getUserAuthority() = softInfoService.authority()
}
