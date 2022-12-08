package pawnrace

class Player(val piece: Piece, var opponent: Player? = null) {
    private fun getAllPawns(board: Board): List<Position> = board.positionsOf(piece)

    private fun getAllValidMoves(game: Game): List<Move> = game.moves(piece)

    fun makeMove(game: Game): Move? {
        fun minimax(game: Game, depth: Int, isMaximising: Boolean): Pair<Move?, Int> {
            fun staticEvaluate(board: Board): Int {
                val pawns = getAllPawns(board);
                // Distance from rank = 1, so white would want to maximise this distance
                return pawns.sumOf { it.rank.index() }
            }

            fun evaluateMoveType(moveType: MoveType): Int = when (moveType) {
                MoveType.PEACEFUL -> 0
                else -> 1
            }

            if (depth == 0 || game.over()) {
                return Pair(null, staticEvaluate(game.getBoard()))
            }
            val moves = if (isMaximising) game.moves(Piece.WHITE) else game.moves(Piece.BLACK)
            if (isMaximising) {
                var maxScore = 0
                var bestMove: Move? = null
                for (move in moves) {
                    game.applyMove(move)
                    val pair = minimax(game, depth - 1, false)
                    val score = pair.second + evaluateMoveType(move.type)
                    if (score > maxScore) {
                        bestMove = move
                        maxScore = score
                    }
                    game.unapplyMove()
                }
                return Pair(bestMove, maxScore);
            } else {
                var minScore = 100;
                var bestMove: Move? = null
                for (move in moves) {
                    game.applyMove(move)
                    val pair = minimax(game, depth - 1, true)
                    val score = pair.second - evaluateMoveType(move.type)
                    if (score < minScore) {
                        bestMove = move
                        minScore = score
                    }
                    game.unapplyMove()
                }
                return Pair(bestMove, minScore)
            }
        }
        return minimax(game, 5, piece == Piece.WHITE).first
    }



    override fun toString(): String = piece.name
}