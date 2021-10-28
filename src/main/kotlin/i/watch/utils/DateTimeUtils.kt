package i.watch.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun formatDateTime(dateTime: LocalDateTime): String {
        return dateTimeFormat.format(dateTime)
    }

    fun formatDate(dateTime: LocalDateTime): String {
        return dateFormat.format(dateTime)
    }

    fun parseDateTime(date: String): LocalDateTime {
        return LocalDateTime.parse(date, dateTimeFormat)
    }

    fun parseDate(date: String): LocalDateTime {
        return LocalDateTime.parse(date, dateFormat)
    }
}
