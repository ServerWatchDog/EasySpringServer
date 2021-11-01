package i.watch.modules.user.service

import i.watch.modules.user.model.view.group.GroupInsertView
import i.watch.modules.user.model.view.group.GroupResultView
import i.watch.utils.template.crud.CRUDService

interface IGroupService : CRUDService<GroupInsertView, GroupResultView, Long> {
    fun getPlain(): List<String>
}
