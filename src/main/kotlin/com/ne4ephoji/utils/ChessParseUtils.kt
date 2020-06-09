package com.ne4ephoji.utils

import com.ne4ephoji.entities.*
import com.ne4ephoji.entities.ChessPosition.Companion.CHESSBOARD_SIZE
import com.ne4ephoji.exception.ChessException

fun String.asFENToPosition(): ChessPosition {
    val chunks = trim().replace("\\s+".toRegex(), " ").split(' ')
    val lines = chunks[0].split('/')
    val fields = lines.toFiguresOnFields()
    val sideToMove = chunks[1].toSide()
    val availableCastlings = chunks[2].toCastlings()
    val enPassantTarget = chunks[3].toChessFieldOrNull()
    val movesSinceLastTakeOrPawnMove = chunks[4].toInt()
    val moveNumber = chunks[5].toInt()

    return ChessPosition(
        figures = fields,
        sideToMove = sideToMove,
        availableCastlings = availableCastlings,
        enPassantTarget = enPassantTarget,
        movesSinceLastTakeOrPawnMove = movesSinceLastTakeOrPawnMove,
        moveNumber = moveNumber
    )
}

fun String.toChessFieldOrNull(): ChessField? {
    return if (length != 2) {
        if (this == "-") null else throw ChessException("Wrong chess field: ${this}!")
    } else {
        toChessField()
    }
}

fun String.toChessField(): ChessField {
    if (length != 2) {
        throw ChessException("Wrong chess field: ${this}!")
    } else {
        val fileNumberInAscii = this[0].toInt()
        val file = if (fileNumberInAscii in 97..122) {
            fileNumberInAscii - 97
        } else throw ChessException("Wrong file: ${this[0]}!")

        val rank = if (this[1].isDigit()) {
            val value = CHESSBOARD_SIZE - this[1].toString().toInt()
            if (value > CHESSBOARD_SIZE) throw ChessException("Wrong rank: ${this[1]}") else value
        } else throw ChessException("Wrong rank: ${this[1]}")

        return ChessField(rank = rank, file = file)
    }
}

private fun List<String>.toFiguresOnFields(): Array<Array<ChessFigure?>> {
    val fields = Array(8) { arrayOfNulls<ChessFigure>(8) }
    var fileNumber = 0
    for (i in indices) {
        for (j in this[i].indices) {
            if (this[i][j].isDigit()) {
                fileNumber += this[i][j].toString().toInt()
            } else {
                val figureType = when (this[i][j]) {
                    'P' -> ChessFigure(ChessFigure.Type.PAWN, ChessSide.WHITE)
                    'N' -> ChessFigure(ChessFigure.Type.KNIGHT, ChessSide.WHITE)
                    'B' -> ChessFigure(ChessFigure.Type.BISHOP, ChessSide.WHITE)
                    'R' -> ChessFigure(ChessFigure.Type.ROOK, ChessSide.WHITE)
                    'Q' -> ChessFigure(ChessFigure.Type.QUEEN, ChessSide.WHITE)
                    'K' -> ChessFigure(ChessFigure.Type.KING, ChessSide.WHITE)
                    'p' -> ChessFigure(ChessFigure.Type.PAWN, ChessSide.BLACK)
                    'n' -> ChessFigure(ChessFigure.Type.KNIGHT, ChessSide.BLACK)
                    'b' -> ChessFigure(ChessFigure.Type.BISHOP, ChessSide.BLACK)
                    'r' -> ChessFigure(ChessFigure.Type.ROOK, ChessSide.BLACK)
                    'q' -> ChessFigure(ChessFigure.Type.QUEEN, ChessSide.BLACK)
                    'k' -> ChessFigure(ChessFigure.Type.KING, ChessSide.BLACK)
                    else -> throw ChessException("Invalid figure type: ${this[i][j]}!")
                }
                fields[i][fileNumber] = figureType
                fileNumber++
            }
        }
        fileNumber = 0
    }
    return fields
}

private fun String.toSide() = when (this) {
    "w" -> ChessSide.WHITE
    "b" -> ChessSide.BLACK
    else -> throw ChessException("Invalid side type: ${this}!")
}

private fun String.toCastlings() = ChessCastlings(
    whiteKingsideCastlingAvailable = contains('K'),
    whiteQueensideCastlingAvailable = contains('Q'),
    blackKingsideCastlingAvailable = contains('k'),
    blackQueensideCastlingAvailable = contains('q')
)