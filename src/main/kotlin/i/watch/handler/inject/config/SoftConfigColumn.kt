package i.watch.handler.inject.config

/**
 * 指定此配置信息
 * @property key String
 * @property defaultValue String
 * @constructor
 */
annotation class SoftConfigColumn(
    val key: String = "",
    val defaultValue: String = "",
    val defaultEnv: String = ""
)
