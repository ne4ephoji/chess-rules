package com.ne4ephoji.entities

import com.ne4ephoji.exception.ChessException

data class ChessPosition(
    val figures: Array<Array<ChessFigure?>>,
    var sideToMove: ChessSide,
    var availableCastlings: ChessCastlings,
    var enPassantTarget: ChessField?,
    var movesSinceLastTakeOrPawnMove: Int,
    var moveNumber: Int
) {
    init {
        if (figures.size != CHESSBOARD_SIZE) throw ChessException("Wrong number of ranks!!!")
        for (i in 0 until CHESSBOARD_SIZE) {
            if (figures[i].size != CHESSBOARD_SIZE) throw ChessException("Wrong number of files!!!")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other === null || other !is ChessPosition || other.hashCode() != this.hashCode()) return false
        for (i in 0 until CHESSBOARD_SIZE) {
            for (j in 0 until CHESSBOARD_SIZE) {
                if (other.figures[i][j] != this.figures[i][j]) return false
            }
        }
        if (other.sideToMove != sideToMove) return false
        if (other.availableCastlings != availableCastlings) return false
        if (other.enPassantTarget != enPassantTarget) return false
        return true
    }

    override fun hashCode(): Int {
        var result = sideToMove.hashCode()
        result = HASHCODE_MULTIPLIER * result + availableCastlings.hashCode()
        result = HASHCODE_MULTIPLIER * result + enPassantTarget.hashCode()
        return result
    }


    companion object {
        const val CHESSBOARD_SIZE = 8
        private const val HASHCODE_MULTIPLIER = 31
    }
}