package i.watch.handler.inject.config

import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass

class ConfigFactoryBean<T : Any>(
    private val interfaceClass: KClass<in T>,
    private val context: ApplicationContext
) : FactoryBean<T> {

    @Suppress("UNCHECKED_CAST")
    override fun getObject(): T {
        val newMapperProxy = ConfigProxyFactory.newMapperProxy(interfaceClass, context)
        return newMapperProxy
    }

    override fun getObjectType(): Class<*> {
        return interfaceClass.java
    }

    override fun isSingleton(): Boolean {
        return true
    }
}
