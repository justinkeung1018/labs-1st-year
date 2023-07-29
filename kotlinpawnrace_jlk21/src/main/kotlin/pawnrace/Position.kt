package pawnrace

import java.lang.IllegalArgumentException

class Position(pos: String) {
    val file = File(pos.lowercase()[0])
    val rank = Rank(pos[1].digitToInt())

    constructor(file: File, rank: Rank) : this("${file.file}${rank.rank}")

    constructor(row: Int, col: Int) : this(File('a' + col), Rank(row + 1))

    override fun toString(): String = "${file.file}${rank.rank}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Position) return false
        return file == other.file && rank == other.rank
    }

    override fun hashCode(): Int {
        var result = file.hashCode()
        result = 31 * result + rank.hashCode()
        return result
    }
}

class File @Throws(IllegalArgumentException::class) constructor(file: Char) {
    val file = if (file.lowercaseChar() < 'a' || file.lowercaseChar() > 'h') {
        throw IllegalArgumentException("File must be between 'a' and 'h'")
    } else {
        file.lowercaseChar()
    }
    fun index(): Int = file - 'a'

    operator fun plus(increment: Int): File = File(file + increment)

    operator fun minus(decrement: Int): File = File(file - decrement)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is File) return false
        return file == other.file
    }

    override fun hashCode(): Int {
        return file.hashCode()
    }
}

class Rank @Throws(IllegalArgumentException::class) constructor(rank: Int) {
    val rank = if (rank < 1 || rank > 8) {
        throw IllegalArgumentException("Rank must be between 1 and 8")
    } else {
        rank
    }

    fun index(): Int = rank - 1

    operator fun plus(increment: Int): Rank = Rank(rank + increment)

    operator fun minus(decrement: Int): Rank = Rank(rank - decrement)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Rank) return false
        return rank == other.rank
    }

    override fun hashCode(): Int {
        return rank
    }
}