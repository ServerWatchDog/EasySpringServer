package i.watch.modules.user.model.view.user

import i.watch.handler.inject.page.IPageView

data class UsersResultView(
    override val data: List<UserResultView>,
    override val index: Long,
    override val pages: Long
) : IPageView<UserResultView>
