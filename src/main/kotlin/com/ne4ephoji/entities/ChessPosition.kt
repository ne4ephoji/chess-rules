package com.ne4ephoji.entities

import com.ne4ephoji.exception.ChessException
import com.ne4ephoji.utils.get
import com.ne4ephoji.utils.set

data class ChessPosition(
    val figures: Array<Array<ChessFigure?>>,
    var sideToMove: ChessSide,
    val availableCastlings: ChessCastlings,
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

    fun makeMove(move: ChessMove) {
        if (figures[move.source]?.type == ChessFigure.Type.KING) {
            when (sideToMove) {
                ChessSide.WHITE -> {
                    availableCastlings.whiteKingsideCastlingAvailable = false
                    availableCastlings.whiteQueensideCastlingAvailable = false
                }
                ChessSide.BLACK -> {
                    availableCastlings.blackKingsideCastlingAvailable = false
                    availableCastlings.blackQueensideCastlingAvailable = false
                }
            }
        }
        when (move) {
            is ChessMove.Castling -> {
                figures[move.target] = figures[move.source]
                figures[move.source] = null
                if (move.target.file > move.source.file) {
                    figures[move.source.rank][move.source.file + 1] = figures[move.source.rank][move.source.file + 3]
                    figures[move.source.rank][move.source.file + 3] = null
                } else {
                    figures[move.source.rank][move.source.file - 1] = figures[move.source.rank][move.source.file - 4]
                    figures[move.source.rank][move.source.file - 4] = null
                }
            }
            is ChessMove.EnPassantTake -> {
                figures[move.target] = figures[move.source]
                when (sideToMove) {
                    ChessSide.WHITE -> figures[move.target.rank + 1][move.target.file] = null
                    ChessSide.BLACK -> figures[move.target.rank - 1][move.target.file] = null
                }
                figures[move.source] = null
            }
            is ChessMove.Movement, is ChessMove.Take -> {
                figures[move.target] = figures[move.source]
                figures[move.source] = null
            }
            is ChessMove.TransformationMovement -> {
                figures[move.target] = move.figure
                figures[move.source] = null
            }
            is ChessMove.TransformationTake -> {
                figures[move.target] = move.figure
                figures[move.source] = null
            }
        }
        sideToMove = when (sideToMove) {
            ChessSide.WHITE -> ChessSide.BLACK
            ChessSide.BLACK -> ChessSide.WHITE
        }
    }

    companion object {
        const val CHESSBOARD_SIZE = 8
        private const val HASHCODE_MULTIPLIER = 31
    }
}