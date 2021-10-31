package i.watch.utils

import java.util.Optional

inline fun <reified T : Any?> Optional<T>.ifEmpty(elseIf: () -> T?): Optional<T> {
    return if (isEmpty) {
        Optional.ofNullable(elseIf())
    } else {
        this
    }
}
