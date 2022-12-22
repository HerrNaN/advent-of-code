package common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class HelpersKtTest {

    @Test
    fun possibleOrders() {
        assertEquals(
            listOf(listOf("A")),
            listOf("A").possibleOrders()
        )

        assertEquals(
            listOf(listOf("A", "B"), listOf("B", "A")),
            listOf("A", "B").possibleOrders()
        )

        assertEquals(
            listOf(
                listOf("A", "B", "C"),
                listOf("A", "C", "B"),
                listOf("B", "A", "C"),
                listOf("B", "C", "A"),
                listOf("C", "A", "B"),
                listOf("C", "B", "A"),
            ),
            listOf("A", "B", "C").possibleOrders()
        )
    }

    @Test
    fun possibleSplitsIn2() {
        assertThrows<IllegalArgumentException> {
            emptySet<String>().possibleSplitsIn2()
        }
        assertThrows<IllegalArgumentException> {
            setOf("A").possibleSplitsIn2()
        }
        assertEquals(
            listOf(
                Pair(setOf("A"), setOf("B", "C")),
                Pair(setOf("B"), setOf("A", "C")),
                Pair(setOf("C"), setOf("B", "A")),
                Pair(setOf("A", "B"), setOf("C")),
                Pair(setOf("A", "C"), setOf("B")),
                Pair(setOf("B", "C"), setOf("A")),
            ).size,
            setOf("A", "B", "C").possibleSplitsIn2().size
        )
    }
}