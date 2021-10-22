package i.watch.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

fun Any.getLogger(): Logger {
    return LoggerFactory.getLogger(this::class.java)
}

fun KClass<*>.getLogger(): Logger {
    return LoggerFactory.getLogger(this.java)
}

fun Class<*>.getLogger(): Logger {
    return LoggerFactory.getLogger(this)
}
