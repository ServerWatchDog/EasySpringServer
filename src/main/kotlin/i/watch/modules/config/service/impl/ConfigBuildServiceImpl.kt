package i.watch.modules.config.service.impl

import i.watch.modules.config.service.IConfigBuildService
import i.watch.utils.getLogger
import org.springframework.stereotype.Service

@Service
class ConfigBuildServiceImpl() : IConfigBuildService {
    override fun updateConfig() {
    }

    companion object {
        private val logger = getLogger()
    }
}
