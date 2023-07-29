package pawnrace

enum class Piece {
    BLACK {
        override fun opponent(): Piece = WHITE
    },

    WHITE {
        override fun opponent(): Piece = BLACK
    };

    abstract fun opponent(): Piece
}