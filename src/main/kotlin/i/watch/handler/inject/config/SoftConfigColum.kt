package i.watch.handler.inject.config

/**
 * 指定此配置信息
 * @property key String
 * @property default String
 * @constructor
 */
annotation class SoftConfigColum(
    val key: String = "",
    val default: String = ""
)
