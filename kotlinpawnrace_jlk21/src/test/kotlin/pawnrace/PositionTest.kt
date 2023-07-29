package pawnrace

import org.junit.Assert.assertEquals
import org.junit.Test

class PositionTest {
    @Test
    fun `constructors are consistent`() {
        val position = Position("a4")
        assertEquals(position, Position("A4"))
        assertEquals(position, Position(File('a'), Rank(4)))
        assertEquals(position, Position(3, 0))
    }
}