package i.watch.utils.cache

import i.watch.handler.inject.session.ISession
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

abstract class LightDBSession(
    private val lightDBMap: LightDBMap,
    private val timeOut: Long,
    private val timeUnit: TimeUnit
) : ISession {
    val createTime: LocalDateTime
        get() = LocalDateTime.from(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(
                lightDBMap.get<String>("_init").get()
            )
        )

    override fun refresh() {
        lightDBMap.expire(timeOut, timeUnit)
        lightDBMap.putValue("_update", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }
}
