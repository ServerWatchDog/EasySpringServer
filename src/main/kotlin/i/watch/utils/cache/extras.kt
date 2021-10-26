package i.watch.utils.cache

import java.util.Optional
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

inline fun <reified T : Any> LightDBMap.get(key: String): Optional<T> {
    return get(key, T::class)
}

/**
 *提供到 LightDB Map  数据绑定
 */
fun <RES : Any> LightDBMap.bind(
    key: String,
    bindClass: KClass<RES>,
    emptyElse: (String) -> RES = { throw NullPointerException("未找到名为 $it 的字段.") }
): ReadWriteProperty<Any?, RES> =
    object : ReadWriteProperty<Any?, RES> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): RES {
            return this@bind.get(key, bindClass).orElse(emptyElse(key))
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: RES) {
            this@bind.putValue(key, value)
        }
    }

inline fun <reified RES : Any> LightDBMap.bind(
    key: String
) = bind(key, RES::class)

inline fun <reified RES : Any> LightDBMap.bind(
    key: String,
    default: RES
) =
    bind(
        key, RES::class
    ) { default }

inline fun <reified RES : Any> LightDBMap.readOnlyBind(key: String) = readOnlyBind(key, RES::class)

/**
 *提供到 LightDB Map  数据绑定
 */
fun <RES : Any> LightDBMap.readOnlyBind(
    key: String,
    bindClass: KClass<RES>
) =
    ReadOnlyProperty<Any?, Optional<RES>> { _, _ ->
        this@readOnlyBind.get(key, bindClass)
    }

/**
 * 利用ID生成器创建一个新的 MAP
 *
 * 循环调用id生成器方法直到不存在相同的ID
 *
 * @receiver LightDB
 * @param func Function0<String> ID 生成器
 * @return LightDBMap
 */
fun LightDB.createMap(func: () -> String): LightDBMap {
    val count = AtomicInteger(0)
    while (true) {
        try {
            return this.createMap(func())
        } catch (e: Exception) {
            if (count.addAndGet(1) > 1000) {
                throw RuntimeException("超过一千次 id碰撞！")
            }
        }
    }
}
