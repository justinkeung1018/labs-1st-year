package pawnrace

import org.junit.Assert.*
import org.junit.Test

class BoardTest {
    @Test
    fun `constructor`() {
        var board = Board(File('a'), File('a'))
        assertNull(board.getBoard()[1][0])
        assertNull(board.getBoard()[6][0])

        board = Board(File('a'), File('h'))
        assertNull(board.getBoard()[1][0])
        assertNull(board.getBoard()[6][7])
    }

    @Test
    fun `can search for pieces`() {
        val board = Board(File('a'), File('h'))
        assertEquals(Piece.WHITE, board.pieceAt(Position(1, 1)))
        assertEquals(Piece.BLACK, board.pieceAt(Position(6, 0)))
        assertNull(board.pieceAt(Position(0, 0)))
        assertNull(board.pieceAt(Position(1, 0))) // Consistent with direct array access
    }

    @Test
    fun `finds positions of all pieces`() {
        val board = Board(File('a'), File('h'))
        assertEquals(
            setOf(
                Position(1, 1),
                Position(1, 2),
                Position(1, 3),
                Position(1, 4),
                Position(1, 5),
                Position(1, 6),
                Position(1, 7)
            ), board.positionsOf(Piece.WHITE).toSet()
        )
        assertEquals(
            setOf(
                Position(6, 0),
                Position(6, 1),
                Position(6, 2),
                Position(6, 3),
                Position(6, 4),
                Position(6, 5),
                Position(6, 6)
            ), board.positionsOf(Piece.BLACK).toSet()
        )
    }

    @Test
    fun `is valid peaceful move`() {
        val board = Board(File('a'), File('h'))
        assertTrue(
            "Can move one step from starting position",
            board.isValidMove(Move(Piece.WHITE, Position(1, 1), Position(2, 1), MoveType.PEACEFUL))
        )
        assertTrue(
            "Can move two steps from starting position",
            board.isValidMove(Move(Piece.WHITE, Position(1, 1), Position(3, 1), MoveType.PEACEFUL))
        )
        assertTrue(
            "Can move black pawn",
            board.isValidMove(Move(Piece.BLACK, Position(6, 0), Position(5, 0), MoveType.PEACEFUL))
        )
        assertTrue(
            "Can move black pawn two steps from starting position",
            board.isValidMove(Move(Piece.BLACK, Position(6, 0), Position(4, 0), MoveType.PEACEFUL))
        )
        assertFalse(
            "Cannot move horizontally",
            board.isValidMove(Move(Piece.WHITE, Position(1, 1), Position(1, 2), MoveType.PEACEFUL))
        )
        assertFalse(
            "Cannot move backwards",
            board.isValidMove(Move(Piece.WHITE, Position(1, 1), Position(0, 1), MoveType.PEACEFUL))
        )
        assertFalse(
            "Cannot move more than two steps",
            board.isValidMove(Move(Piece.WHITE, Position(1, 1), Position(4, 1), MoveType.PEACEFUL))
        )
        assertFalse(
            "Cannot move diagonally (capture move is not peaceful move)",
            board.isValidMove(
                Move(Piece.WHITE, Position(1, 1), Position(2, 2), MoveType.PEACEFUL)
            )
        )
        assertFalse(
            "Cannot move pawn belonging to opponent",
            board.isValidMove(Move(Piece.WHITE, Position(6, 0), Position(5, 0), MoveType.PEACEFUL))
        )
        // Change the state of the board:
        // - Cannot move forward when occupied by 1) own pawn 2) opponent pawn
        // - Cannot move forward out of the board (out of bounds)
    }

    @Test
    fun `is valid capture move`() {
        val board = Board(File('a'), File('a'))
        // This is an invalid move solely for convenience
        board.move(Move(Piece.WHITE, Position(1, 1), Position(5, 1), MoveType.PEACEFUL))

        assertTrue(
            "White pawn captures diagonally right",
            board.isValidMove(Move(Piece.WHITE, Position(5, 1), Position(6, 2), MoveType.CAPTURE))
        )
        assertFalse(
            "Empty target position",
            board.isValidMove(Move(Piece.WHITE, Position(5, 1), Position(6, 0), MoveType.CAPTURE))
        )
        assertTrue(
            "Black pawn captures diagonally left (relative to the board, not the player)",
            board.isValidMove(Move(Piece.BLACK, Position(6, 2), Position(5, 1), MoveType.CAPTURE))
        )
    }

    @Test
    fun `is valid en passant move`() {
        val board = Board(File('a'), File('a'))
        // The following move is invalid solely for convenience
        board.move(Move(Piece.WHITE, Position(1, 1), Position(4, 1), MoveType.PEACEFUL))
        val blackStarting = Move(Piece.BLACK, Position(6, 2), Position(4, 2), MoveType.PEACEFUL)
        board.move(blackStarting)


        val enPassant = Move(Piece.WHITE, Position(4, 1), Position(5, 2), MoveType.EN_PASSANT)
        assertTrue(
            "White captures black en passant",
            board.isValidMove(enPassant, blackStarting)
        )
        assertFalse(
            "White does not capture black if not en passant",
            board.isValidMove(enPassant)
        )
        board.move(enPassant)
        assertEquals("Black pawn is removed", 6, board.positionsOf(Piece.BLACK).size)
    }
}