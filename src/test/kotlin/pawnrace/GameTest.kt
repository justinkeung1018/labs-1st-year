package pawnrace

import org.junit.Assert.*
import org.junit.Test

class GameTest {
    @Test
    fun `can count number of moves`() {
        val board = Board(File('a'), File('h'))
        val game = Game(board, Player(Piece.WHITE))
        assertEquals(14, game.moves(Piece.WHITE).size)

        val whites = board.positionsOf(Piece.WHITE)
        for (i in 0 until 7) {
            board.move(Move(Piece.WHITE, whites[i], Position(7, i), MoveType.PEACEFUL))
        }
        assertEquals(0, game.moves(Piece.WHITE).size)
    }

    @Test
    fun `determine if game is over`() {
        val board = Board(File('a'), File('h'))
        val game = Game(board, Player(Piece.WHITE))
        assertFalse("Game is not over when it has just begun", game.over())

        board.move(Move(Piece.WHITE, Position(1, 1), Position(7, 1), MoveType.PEACEFUL))
        assertTrue("Game is over when one pawn reached the end", game.over())

        board.move(Move(Piece.WHITE, Position(1, 2), Position(7, 1), MoveType.PEACEFUL))
        assertTrue("Game is over when more than one pawn reached the end (impossible in gameplay)", game.over())

        // TODO: Add test for stalemate
    }

    @Test
    fun `can parse move`() {
        val board = Board(File('a'), File('h'))
        val white = Player(Piece.WHITE)
        val black = Player(Piece.BLACK)
        white.opponent = black
        black.opponent = white
        val game = Game(board, white)

        assertEquals(
            "Valid peaceful move with two steps",
            Move(Piece.WHITE, Position(1, 1), Position(3, 1), MoveType.PEACEFUL),
            game.parseMove("b4")
        )
        assertEquals(
            "Valid peaceful move with one step",
            Move(Piece.WHITE, Position(1, 1), Position(2, 1), MoveType.PEACEFUL),
            game.parseMove("b3")
        )

        board.move(Move(Piece.WHITE, Position(1, 1), Position(5, 1), MoveType.PEACEFUL))
        assertEquals(
            "Valid capture move",
            Move(Piece.WHITE, Position(5, 1), Position(6, 2), MoveType.CAPTURE),
            game.parseMove("bxc7")
        )

        board.move(Move(Piece.WHITE, Position(1, 3), Position(5, 3), MoveType.PEACEFUL))
        assertEquals(
            "Differentiate between potential capture moves",
            Move(Piece.WHITE, Position(5, 3), Position(6, 2), MoveType.CAPTURE),
            game.parseMove("dxc7")
        )

        board.move(Move(Piece.WHITE, Position(1, 4), Position(4, 4), MoveType.PEACEFUL))
        // Some random move to set player to black
        game.applyMove(Move(Piece.WHITE, Position(1, 6), Position(3, 6), MoveType.PEACEFUL))
        game.applyMove(Move(Piece.BLACK, Position(6, 5), Position(4, 5), MoveType.PEACEFUL))
        val res = game.parseMove("exf6")
        assertEquals(
            "Valid en Passant move",
            Move(Piece.WHITE, Position(4, 4), Position(5, 5), MoveType.EN_PASSANT),
            game.parseMove("exf6")
        )
    }
}