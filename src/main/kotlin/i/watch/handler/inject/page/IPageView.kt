package i.watch.handler.inject.page

interface IPageView<T : Any> {
    /**
     * 数据
     */
    val data: List<T>

    /**
     * 当前页码
     */
    val index: Long

    /**
     * 页码总量
     */
    val pages: Long
}
