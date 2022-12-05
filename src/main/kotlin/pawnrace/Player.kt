package pawnrace

class Player(val piece: Piece, var opponent: Player? = null) {
    fun getAllPawns(board: Board): List<Position> = board.positionsOf(piece)

    fun getAllValidMoves(game: Game): List<Move> = game.moves(piece)

    fun makeMove(game: Game): Move? {
        val moves = game.moves(piece)
        return if (moves.isEmpty()) {
            null
        } else {
            moves[(moves.indices).random()]
        }
    }
}