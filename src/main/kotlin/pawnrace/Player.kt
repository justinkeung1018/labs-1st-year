package pawnrace

class Player(val piece: Piece, var opponent: Player? = null) {
    private fun getAllPawns(board: Board): List<Position> = board.positionsOf(piece)

    private fun getAllValidMoves(game: Game): List<Move> = game.moves(piece)

    fun makeMove(game: Game): Move? {
        fun minimax(game: Game, depth: Int, alpha: Int, beta: Int, isMaximising: Boolean): Pair<Move?, Int> {
            fun staticEvaluate(board: Board): Int {
                val pawns = getAllPawns(board)
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
                var maxScore = Int.MIN_VALUE
                var bestMove: Move? = null
                var currAlpha = alpha
                for (move in moves) {
                    game.applyMove(move)
                    val pair = minimax(game, depth - 1, currAlpha, beta, false)
                    val score = pair.second + evaluateMoveType(move.type)
                    currAlpha = currAlpha.coerceAtLeast(score)
                    if (score > maxScore) {
                        bestMove = move
                        maxScore = score
                    }
                    game.unapplyMove()
                    if (beta <= currAlpha) {
                        break
                    }
                }
                return Pair(bestMove, maxScore)
            } else {
                var minScore = Int.MAX_VALUE
                var bestMove: Move? = null
                var currBeta = beta
                for (move in moves) {
                    game.applyMove(move)
                    val pair = minimax(game, depth - 1, alpha, currBeta,true)
                    val score = pair.second - evaluateMoveType(move.type)
                    currBeta = currBeta.coerceAtMost(score)
                    if (score < minScore) {
                        bestMove = move
                        minScore = score
                    }
                    game.unapplyMove()
                    if (currBeta <= alpha) {
                        break
                    }
                }
                return Pair(bestMove, minScore)
            }
        }
        return minimax(game, 6, Integer.MIN_VALUE, Integer.MAX_VALUE, piece == Piece.WHITE).first
    }



    override fun toString(): String = piece.name
}