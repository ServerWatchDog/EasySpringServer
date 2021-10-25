package i.watch.modules.config.service

/**
 * 配置预加载
 */
interface IConfigBuildService {
    /**
     * 启动时载入第三方配置
     */
    fun updateConfig()
}
