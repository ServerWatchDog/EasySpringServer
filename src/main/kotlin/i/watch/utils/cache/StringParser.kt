package i.watch.utils.cache

import kotlin.reflect.KClass

/**
 *  字符串解析器
 */
interface StringParser {
    /**
     * 将对象转换成字符串
     */
    fun toString(data: Any): String

    /**
     * 字符串转换成对象
     */
    fun <T : Any> fromString(data: String, clazz: KClass<T>): T
}
