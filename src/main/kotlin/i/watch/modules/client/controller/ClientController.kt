package i.watch.modules.client.controller

import i.watch.handler.inject.session.Permission
import i.watch.modules.client.model.view.client.ClientInsertView
import i.watch.modules.client.model.view.client.ClientResultView
import i.watch.modules.client.service.IClientService
import i.watch.modules.user.UserAuthority
import i.watch.utils.template.PageView
import i.watch.utils.template.SimpleView
import i.watch.utils.template.crud.CRUDApi
import i.watch.utils.template.crud.CRUDController
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("\${global.server.api}/admin/client")
@RestController
class ClientController(service: IClientService) : CRUDApi<ClientInsertView, ClientResultView, Long>,
    CRUDController<ClientInsertView, ClientResultView, Long>(service) {
    @Permission("user", [UserAuthority.CLIENT_ADMIN_WRITE])
    override fun insert(input: ClientInsertView): ClientResultView {
        return super.insert(input)
    }

    @Permission("user", [UserAuthority.CLIENT_ADMIN_READ])
    override fun getAll(pageable: Pageable): PageView<ClientResultView> {
        return super.getAll(pageable)
    }

    @Permission("user", [UserAuthority.CLIENT_ADMIN_WRITE])
    override fun delete(id: Long): SimpleView<Boolean> {
        return super.delete(id)
    }

    @Permission("user", [UserAuthority.CLIENT_ADMIN_WRITE])
    override fun update(id: Long, input: ClientInsertView): ClientResultView {
        return super.update(id, input)
    }
}
