package pawnrace

import java.lang.IllegalArgumentException

class Game(private val board: Board, var player: Player, private val moves: MutableList<Move> = mutableListOf()) {
    fun applyMove(move: Move): Game {
        moves.add(0, move)
        board.move(move)
        player = player.opponent!!
        return this
    }

    fun unapplyMove() {
        if (moves.isEmpty()) {
            return
        }
        val lastMove = moves.removeFirst()
        board.move(Move(lastMove.piece, lastMove.to, lastMove.from, lastMove.type))
        player = player.opponent!!
    }

    fun moves(piece: Piece): List<Move> {
        val moves = mutableListOf<Move>()
        val size = board.getSize()
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (board.getBoard()[i][j] == piece) {
                    val position = Position(i, j)
                    val candidates = listOf(
                        moveForwardBy(position, 1, piece),
                        moveForwardBy(position, 2, piece),
                        moveDiagonalBy(position, true, piece, MoveType.CAPTURE),
                        moveDiagonalBy(position, true, piece, MoveType.EN_PASSANT),
                        moveDiagonalBy(position, false, piece, MoveType.CAPTURE),
                        moveDiagonalBy(position, false, piece, MoveType.EN_PASSANT)
                    )
                    moves.addAll(candidates.filterNotNull())
                }
            }
        }
        return moves
    }

    private fun moveForwardBy(from: Position, step: Int, piece: Piece): Move? {
        val toRank = when (piece) {
            Piece.WHITE -> from.rank + 1
            Piece.BLACK -> from.rank - 1
        }
        val to = Position(from.file, toRank)
        val move = Move(piece, from, to, MoveType.PEACEFUL)
        return if (board.isValidMove(move)) move else null
    }

    private fun moveDiagonalBy(from: Position, isLeft: Boolean, piece: Piece, type: MoveType): Move? {
        val toRank = when (piece) {
            Piece.WHITE -> from.rank + 1
            Piece.BLACK -> from.rank - 1
        }
        // isLeft is absolute, not relative to the player
        val toFile = if (isLeft) from.file - 1 else from.file + 1
        val to = Position(toFile, toRank)
        val move = Move(piece, from, to, type)
        return if (board.isValidMove(move)) move else null
    }

    fun over(): Boolean {
        val won = board.getBoard().first().any { it != null } || board.getBoard().last().any{ it != null }
        val stalemate = moves(player.piece).isEmpty()
        return won || stalemate
    }

    fun winner(): Player? {
        val won = board.getBoard().first().any { it != null } || board.getBoard().last().any{ it != null }
        val stalemate = moves(player.piece).isEmpty()
        return if (won) {
            player
        } else if (stalemate) {
            null
        } else {
            null
        }
    }

    // Assume the move is made by the current player
    fun parseMove(san: String): Move? {
        if (san.length != 2 && san.length != 4) {
            return null
        }

        val isCapture = san.length == 4
        val piece = player.piece
        var candidates = board.positionsOf(piece)
        if (isCapture) {
            val fromFile = File(san[0])
            candidates = candidates.filter { it.file == fromFile }
        }

        val to = try {
            Position(san.takeLast(2))
        } catch (e: IllegalArgumentException) {
            return null
        }

        for (position in candidates) {
            if (isCapture) {
                for (type in listOf(MoveType.CAPTURE, MoveType.EN_PASSANT)) {
                    val move = Move(piece, position, to, type)
                    val lastMove = moves.firstOrNull()
                    if (board.isValidMove(move, lastMove)) {
                        return move
                    }
                }
            } else {
                val move = Move(piece, position, to, MoveType.PEACEFUL)
                if (board.isValidMove(move)) {
                    return move
                }
            }
        }
        return null
    }
}