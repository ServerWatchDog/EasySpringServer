package i.watch.modules.install.service.impl

import i.watch.utils.RedisUtils
import java.util.concurrent.TimeUnit

class InstallerSession(redisMapUtils: RedisUtils.RedisMapUtils) :
    RedisUtils.RedisSession(redisMapUtils, 1, TimeUnit.DAYS)
