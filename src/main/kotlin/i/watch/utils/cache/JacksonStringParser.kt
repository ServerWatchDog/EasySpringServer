package i.watch.utils.cache

import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class JacksonStringParser(
    private val objectMapper: ObjectMapper
) : StringParser {
    override fun toString(data: Any): String {
        if (data::class.isSubclassOf(CharSequence::class)) {
            return data.toString()
        }
        return objectMapper.writeValueAsString(data)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> fromString(data: String, clazz: KClass<T>): T {
        return if (clazz.isSubclassOf(CharSequence::class)) {
            data as T
        } else {
            objectMapper.readValue(
                data,
                clazz.javaObjectType
            )
        }
    }
}
