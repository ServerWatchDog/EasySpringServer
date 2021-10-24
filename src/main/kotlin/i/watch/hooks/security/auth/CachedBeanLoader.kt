package i.watch.hooks.security.auth

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

@Component
class CachedBeanLoader(private val applicationContext: ApplicationContext) {
    private val map: MutableMap<String, ISessionService> = ConcurrentHashMap(10)

    @Suppress("UNCHECKED_CAST")
    fun <T : ISessionService> getBean(name: String, clazz: KClass<T>): T {
        return map.getOrPut(name) {
            applicationContext.getBean(name, clazz.java)
        } as T
    }
}
