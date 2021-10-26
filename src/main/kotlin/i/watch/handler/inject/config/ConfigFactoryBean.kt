package i.watch.handler.inject.config

import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf

class ConfigFactoryBean<T : Any>(
    private val interfaceClass: KClass<in T>,
    private val context: ApplicationContext
) : FactoryBean<T> {

    @Suppress("UNCHECKED_CAST")
    override fun getObject(): T {
        val newMapperProxy = ConfigProxyFactory.newMapperProxy(interfaceClass, context)
        println(newMapperProxy::class.isSubclassOf(interfaceClass))
        println(newMapperProxy::class.isSuperclassOf(interfaceClass))
        return newMapperProxy
    }

    override fun getObjectType(): Class<*> {
        return interfaceClass.java
    }

    override fun isSingleton(): Boolean {
        return true
    }
}
