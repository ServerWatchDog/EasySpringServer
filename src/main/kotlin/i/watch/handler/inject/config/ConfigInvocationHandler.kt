package i.watch.handler.inject.config

import i.watch.modules.config.service.IConfigService
import i.watch.utils.cache.KeyExistsException
import i.watch.utils.cache.StringParser
import i.watch.utils.getLogger
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.jvm.javaSetter
import kotlin.reflect.jvm.jvmErasure

class ConfigInvocationHandler(
    superClass: KClass<out Any>,
    val context: ApplicationContext
) : InvocationHandler {
    private val cache: MutableMap<String, Any> = ConcurrentHashMap()
    private val configService: IConfigService by lazy {
        context.getBean(IConfigService::class.java)
    }

    private val stringParser: StringParser by lazy {
        context.getBean(StringParser::class.java)
    }
    private val environment: Environment by lazy {
        context.getBean(Environment::class.java)
    }

    inner class Reader<T : Any>(
        private val key: String,
        private val clazz: KClass<T>,
        def: String,
        defEnv: String,

    ) {
        private val defaultValue: String by lazy {
            if (defEnv.isNotBlank()) {
                environment[defEnv] ?: def
            } else {
                def
            }
        }

        fun get(): T {
            return if (defaultValue.isNotBlank()) {
                configService.getCachedString(key = key, defaultValue)
                    .map { stringParser.fromString(it, clazz) }.get()
            } else {
                configService.getString(key = key)
                    .map { stringParser.fromString(it, clazz) }
                    .orElseThrow {
                        throw KeyExistsException("Key $key 不存在.")
                    }
            }
        }
    }

    inner class Writer(
        private val key: String
    ) {
        fun write(value: Any) {
            configService.setString(key, stringParser.toString(value))
        }
    }

    init {
        getLogger().debug("proxy {} init.", superClass)
        val softConfig = AnnotationUtils.findAnnotation(superClass.java, SoftConfig::class.java)
            ?: throw RuntimeException("未找到类 $superClass 下 SoftConfig 注解.")
        superClass.memberProperties
        superClass.memberProperties.forEach {
            val resultType = it.returnType.jvmErasure
            val softConfigColumn = it.findAnnotation<SoftConfigColumn>()
            if (softConfigColumn != null && softConfigColumn.defaultValue.isNotBlank()) {
                try {
                    stringParser.fromString(softConfigColumn.defaultValue, resultType)
                } catch (e: Exception) {
                    throw IllegalArgumentException(
                        "无法将 ${softConfigColumn.defaultValue} 转换成类型：$resultType", e
                    )
                }
            }
            it.javaGetter?.run {

                if (softConfigColumn != null) {
                    cache[name] = Reader(
                        softConfig.name + "." + softConfigColumn.key.ifBlank { it.name },
                        resultType,
                        softConfigColumn.defaultValue,
                        softConfigColumn.defaultEnv
                    )
                } else {
                    cache[name] = Reader(
                        softConfig.name + "." + it.name,
                        resultType,
                        "",
                        ""
                    )
                }
            }

            if (it is KMutableProperty<*>) {
                // 可写入
                it.javaSetter?.run {
                    if (softConfigColumn != null) {
                        cache[name] =
                            Writer(softConfig.name + "." + softConfigColumn.key.ifBlank { it.name })
                    } else {
                        cache[name] = Writer(softConfig.name + "." + it.name)
                    }
                }
            }
        }
    }

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
        val func = cache[method.name] ?: return Unit
        if (func is Reader<*>) {
            return func.get()
        } else if (func is Writer && args != null && args.isNotEmpty()) {
            return func.write(args[0])
        }
        return Unit
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T : Any> newMapperProxy(interfaceClass: KClass<in T>, context: ApplicationContext): T {
            return Proxy.newProxyInstance(
                interfaceClass.java.classLoader,
                arrayOf(interfaceClass.java),
                ConfigInvocationHandler(interfaceClass, context)
            ) as T
        }
    }
}
