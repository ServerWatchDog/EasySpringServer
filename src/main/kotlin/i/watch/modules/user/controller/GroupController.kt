package i.watch.modules.user.controller

import i.watch.handler.inject.session.Permission
import i.watch.modules.user.UserAuthority
import i.watch.modules.user.model.view.group.GroupInsertView
import i.watch.modules.user.model.view.group.GroupResultView
import i.watch.modules.user.service.IGroupService
import i.watch.utils.template.crud.CRUDApi
import i.watch.utils.template.crud.CRUDController
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${global.server.api}/admin/group")
class GroupController(
    private val groupService: IGroupService
) : CRUDController<GroupInsertView, GroupResultView, Long>(groupService),
    CRUDApi<GroupInsertView, GroupResultView, Long> {
    @Permission("user", [UserAuthority.USER_ADMIN_READ])
    override fun getAll(pageable: Pageable) = super.getAll(pageable)

    @Permission("user", [UserAuthority.USER_ADMIN_READ])
    @GetMapping("plain")
    fun getAll() = groupService.getPlain()

    @Permission("user", [UserAuthority.USER_ADMIN_WRITE])
    override fun insert(input: GroupInsertView) = super.insert(input)

    @Permission("user", [UserAuthority.USER_ADMIN_WRITE])
    override fun update(id: Long, input: GroupInsertView) = super.update(id, input)

    @Permission("user", [UserAuthority.USER_ADMIN_WRITE])
    override fun delete(id: Long) = super.delete(id)
}
