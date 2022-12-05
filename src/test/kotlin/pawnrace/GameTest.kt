package pawnrace

import org.junit.Assert.assertEquals
import org.junit.Test

class GameTest {
    @Test
    fun `can count number of moves`() {
        val board = Board(File('a'), File('h'))
        val game = Game(board, Player(Piece.WHITE))
        assertEquals(14, game.moves(Piece.WHITE).size)
    }
}