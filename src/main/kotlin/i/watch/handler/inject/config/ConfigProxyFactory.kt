package i.watch.handler.inject.config

import i.watch.utils.getLogger
import org.slf4j.MarkerFactory
import org.springframework.cglib.proxy.InvocationHandler
import org.springframework.cglib.proxy.Proxy
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

class ConfigProxyFactory(
    private val superClass: KClass<out Any>,
    val context: ApplicationContext
) : InvocationHandler {
    private val logger = getLogger()
    private val marker = MarkerFactory.getMarker(superClass.qualifiedName)

    init {
        val softConfig = AnnotationUtils.findAnnotation(superClass.java, SoftConfig::class.java)
            ?: throw RuntimeException("未找到类 $superClass 下 SoftConfig 注解.")
        logger.debug(marker, "{}", superClass.declaredMemberProperties)
    }

    override fun invoke(p0: Any?, p1: Method?, p2: Array<out Any>?): Any {
        getLogger().apply {
            info("{}", p0?.javaClass)
            info("{}", p1)
            info("{}", p2)
            info("注入成功！")
        }
        return "Inject"
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T : Any> newMapperProxy(interfaceClass: KClass<in T>, context: ApplicationContext): T {
            return Proxy.newProxyInstance(
                interfaceClass.java.classLoader,
                arrayOf(interfaceClass.java),
                ConfigProxyFactory(interfaceClass, context)
            ) as T
        }
    }
}
