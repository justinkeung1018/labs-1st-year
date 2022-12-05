package pawnrace

class Move(val piece: Piece, val from: Position, val to: Position, val type: MoveType) {
    override fun toString(): String = to.toString()
}