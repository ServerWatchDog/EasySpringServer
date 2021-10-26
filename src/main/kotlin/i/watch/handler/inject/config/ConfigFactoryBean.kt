package i.watch.handler.inject.config

import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass

class ConfigFactoryBean<T : Any>(
    private val interfaceClass: KClass<in T>,
    private val context: ApplicationContext
) : FactoryBean<T> {

    override fun getObject(): T {
        return ConfigInvocationHandler.newMapperProxy(interfaceClass, context)
    }

    override fun getObjectType(): Class<*> {
        return interfaceClass.java
    }

    override fun isSingleton(): Boolean {
        return true
    }
}
