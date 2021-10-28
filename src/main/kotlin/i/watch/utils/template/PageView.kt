package i.watch.utils.template

data class PageView<T : Any>(
    /**
     * 数据
     */
    val data: List<T>,

    /**
     * 当前页码
     */
    val index: Int,

    /**
     * 页码总量
     */
    val pages: Long,
)
