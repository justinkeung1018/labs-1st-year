package pawnrace

class Move(val piece: Piece, val from: Position, val to: Position, val type: MoveType) {
    override fun toString(): String = to.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Move) return false
        return piece == other.piece && from == other.from && to == other.to && type == other.type
    }

    override fun hashCode(): Int {
        var result = piece.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + to.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}