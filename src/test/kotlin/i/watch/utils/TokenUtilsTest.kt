package i.watch.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class TokenUtilsTest {

    @Test
    fun randomToken() {
        for (l in 0..20) {
            val random = String.format("%-5s", Random.nextBoolean().toString())
            val token = TokenUtils.randomToken("$random token-header")
            assertEquals(random, TokenUtils.getTokenHeader(token))
        }
    }
}
