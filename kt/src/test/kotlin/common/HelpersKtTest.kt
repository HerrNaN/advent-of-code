package common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class HelpersKtTest {

    @Test
    fun unionPlus() {
        assertEquals(
            mapOf(
                "A" to 1,
                "B" to 2,
                "C" to 3,
                "D" to 4
            ),
            mapOf(
                "B" to 1,
                "C" to 1,
                "D" to 4
            ) unionPlus mapOf(
                "A" to 1,
                "B" to 1,
                "C" to 2
            )
        )
    }

    @Test
    fun unionMinus() {
        assertEquals(
            mapOf(
                "A" to -1,
                "B" to 1,
                "C" to 1,
                "D" to 4
            ),
            mapOf(
                "B" to 2,
                "C" to 3,
                "D" to 4
            ) unionMinus mapOf(
                "A" to 1,
                "B" to 1,
                "C" to 2
            )
        )
    }

    @Test
    fun scalarTimes() {
        assertEquals(
            mapOf(
                "A" to 2,
                "B" to 6,
                "C" to 10
            ),
            mapOf(
                "A" to 1,
                "B" to 3,
                "C" to 5
            ) scalarTimes 2
        )
    }
}