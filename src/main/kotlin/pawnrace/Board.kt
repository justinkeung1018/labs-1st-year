package pawnrace

class Board(whiteGap: File, blackGap: File) {
    private val size = 8
    private val board: Array<Array<Piece?>> = Array(size) { arrayOfNulls(size) }

    init {
        val whiteRank = Rank(2)
        val blackRank = Rank(7)
        board[whiteRank.index()] = Array(size) { Piece.WHITE }
        board[whiteRank.index()][whiteGap.index()] = null
        board[blackRank.index()] = Array(size) { Piece.BLACK }
        board[blackRank.index()][blackGap.index()] = null
    }

    fun pieceAt(pos: Position): Piece? = board[pos.rank.index()][pos.file.index()]

    fun positionsOf(piece: Piece): List<Position> {
        val positions = mutableListOf<Position>()
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (board[i][j] == piece) {
                    positions.add(Position(i, j))
                }
            }
        }
        return positions
    }

    fun isValidMove(move: Move, lastMove: Move? = null): Boolean {
        val from = move.from
        val fromRankIndex = from.rank.index()
        val fromFileIndex = from.file.index()
        if (fromRankIndex < 0 || fromRankIndex >= size || fromFileIndex < 0 || fromFileIndex > size) {
            return false
        }

        val to = move.to
        val toRankIndex = to.rank.index()
        val toFileIndex = to.file.index()
        if (toRankIndex < 0 || toRankIndex >= size || toFileIndex < 0 || toFileIndex >= size) {
            return false
        }

        val piece = move.piece
        if (board[fromRankIndex][fromFileIndex] != piece) {
            return false
        }

        return when (move.type) {
            MoveType.PEACEFUL -> isValidPeacefulMove(move)
            MoveType.CAPTURE -> isValidCaptureMove(move)
            MoveType.EN_PASSANT -> isValidEnPassantMove(move, lastMove)
        }
    }

    fun move(move: Move): Board {
        if (move.type == MoveType.EN_PASSANT) {
            val rank = when (move.piece.opponent()) {
                Piece.WHITE -> move.to.rank.index() + 1
                Piece.BLACK -> move.to.rank.index() - 1
            }
            board[rank][move.to.file.index()] = null
        }
        board[move.from.rank.index()][move.from.file.index()] = null
        board[move.to.rank.index()][move.to.file.index()] = move.piece
        return this
    }

    fun isPassedPawn(pos: Position): Boolean {
        // Consider the file of the pawn and the two adjacent files.
        // A pawn is a passed pawn if there are no opponent pawns
        // strictly in front of it in the three files, and
        // any own pawn in front of it in the three files are also passed pawns.
        val piece = pieceAt(pos) ?: return false

        val isInFrontAndAdjacent = fun(opponentPosition: Position): Boolean {
            val adjacent = opponentPosition.file.index() - pos.file.index() <= 1
            val front = when (piece) {
                Piece.WHITE -> opponentPosition.rank.index() > pos.rank.index()
                Piece.BLACK -> opponentPosition.rank.index() < pos.rank.index()
            }
            return adjacent && front
        }

        if (positionsOf(piece.opponent()).any(isInFrontAndAdjacent)) {
            return false
        }

        val allies = positionsOf(piece).filter(isInFrontAndAdjacent)
        return allies.all { isPassedPawn(it) }
    }

    fun getSize(): Int = size

    fun getBoard(): Array<Array<Piece?>> = board

    override fun toString(): String {
        val pieceToString = fun(piece: Piece?): String = when (piece) {
            null -> "."
            Piece.BLACK -> "B"
            Piece.WHITE -> "W"
        }
        return board.joinToString("\n") { it.joinToString(" ", transform = pieceToString) }
    }

    private fun isValidPeacefulMove(move: Move): Boolean {
        val horizontalSteps = horizontalSteps(move)
        val verticalSteps = verticalSteps(move)

        // Cannot move horizontally
        if (horizontalSteps != 0) {
            return false
        }

        // Can only move forward (not backward, or stay in same position)
        if (verticalSteps <= 0) {
            return false
        }

        // Target position must be empty
        if (board[move.to.rank.index()][move.to.file.index()] != null) {
            return false
        }

        // If moving two steps, passed-through position must also be empty
        if (verticalSteps == 2) {
            val passedThroughRank = (move.from.rank.index() + move.to.rank.index()) / 2
            if (board[passedThroughRank][move.to.file.index()] != null) {
                return false
            }
        }

        val fromRankIndex = move.from.rank.index()
        val isStarting = when (move.piece) {
            Piece.WHITE -> fromRankIndex == Rank(2).index()
            Piece.BLACK -> fromRankIndex == Rank(7).index()
        }

        if (isStarting && verticalSteps > 2) {
            return false
        } else if (!isStarting && verticalSteps != 1) {
            return false
        }

        return true
    }

    private fun isValidCaptureMove(move: Move): Boolean {
        // Either move left by one or move right by one
        val horizontalSteps = horizontalSteps(move)
        if (horizontalSteps != -1 && horizontalSteps != 1) {
            return false
        }

        // Move forward by one
        if (verticalSteps(move) != 1) {
            return false
        }

        // Target position must be occupied by opponent pawn
        val piece = move.piece
        if (board[move.to.rank.index()][move.to.file.index()] != piece.opponent()) {
            return false
        }

        return true
    }

    private fun isValidEnPassantMove(move: Move, lastMove: Move?): Boolean {
        if (lastMove == null) {
            return false
        }

        if (lastMove.type != MoveType.PEACEFUL) {
            return false
        }

        val horizontalSteps = horizontalSteps(lastMove)
        if (horizontalSteps != 0) {
            return false
        }

        val verticalSteps = verticalSteps(lastMove)
        if (verticalSteps != 2) {
            return false
        }

        val passedThroughRank = (lastMove.to.rank.index() + lastMove.from.rank.index()) / 2
        board[passedThroughRank][lastMove.to.file.index()] = move.piece.opponent()
        val isValidEnPassantMove = isValidCaptureMove(move)
        board[passedThroughRank][lastMove.to.file.index()] = null

        return isValidEnPassantMove
    }

    private fun verticalSteps(move: Move): Int {
        return when (move.piece) {
            Piece.WHITE -> move.to.rank.index() - move.from.rank.index()
            Piece.BLACK -> -(move.to.rank.index() - move.from.rank.index())
        }
    }

    private fun horizontalSteps(move: Move): Int = move.to.file.index() - move.from.file.index()
}