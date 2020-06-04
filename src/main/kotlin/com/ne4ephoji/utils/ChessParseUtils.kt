package com.ne4ephoji.utils

import com.ne4ephoji.entities.*

fun String.asFENToPosition(): ChessPosition {
    val chunks = trim().replace("\\s+".toRegex(), " ").split(' ')
    val lines = chunks[0].split('/')
    val figuresOnFields = lines.toFiguresOnFields()
    val sideToMove = chunks[1].toSide()
    val availableCastlings = chunks[2].toCastlings()
    val enPassantTarget = chunks[3].toChessFieldOrNull()
    val movesSinceLastTakeOrPawnMove = chunks[4].toInt()
    val moveNumber = chunks[5].toInt()

    figuresOnFields.forEach {
        if (it.field.file !in lines.indices || it.field.rank !in lines.indices) {
            throw IllegalArgumentException()
        }
    }

    if (enPassantTarget != null) {
        if (enPassantTarget.rank !in 2..(lines.size - 3) || enPassantTarget.file !in lines.indices) {
            throw IllegalArgumentException()
        }
    }

    return ChessPosition(
        size = lines.size,
        figuresOnFields = figuresOnFields,
        sideToMove = sideToMove,
        availableCastlings = availableCastlings,
        enPassantTarget = enPassantTarget,
        movesSinceLastTakeOrPawnMove = movesSinceLastTakeOrPawnMove,
        moveNumber = moveNumber
    )
}

fun String.toChessFieldOrNull(): ChessField? {
    return if (length != 2) {
        if (this == "-") null else throw IllegalArgumentException()
    } else {
        toChessField()
    }
}

fun String.toChessField(): ChessField {
    if (length != 2) {
        throw IllegalArgumentException()
    } else {
        val fileNumberInAscii = this[0].toInt()
        val file = if (fileNumberInAscii in 97..122) {
            fileNumberInAscii - 96
        } else throw IllegalArgumentException()

        val rank = if (this[1].isDigit()) {
            val value = this[1].toString().toInt()
            if (value < 0) throw IllegalArgumentException() else value + 1
        } else throw IllegalArgumentException()

        return ChessField(rank = rank, file = file)
    }
}

private fun List<String>.toFiguresOnFields(): HashSet<ChessFigureOnField> {
    val figuresOnFields = HashSet<ChessFigureOnField>()
    var fileNumber = 0
    for (i in indices) {
        for (j in this[i].indices) {
            if (this[i][j].isDigit()) {
                fileNumber += this[i][j].toString().toInt()
            } else {
                val figure = when (this[i][j]) {
                    'P' -> ChessFigure.WHITE_PAWN
                    'N' -> ChessFigure.WHITE_KNIGHT
                    'B' -> ChessFigure.WHITE_BISHOP
                    'R' -> ChessFigure.WHITE_ROOK
                    'Q' -> ChessFigure.WHITE_QUEEN
                    'K' -> ChessFigure.WHITE_KING
                    'p' -> ChessFigure.BLACK_PAWN
                    'n' -> ChessFigure.BLACK_KNIGHT
                    'b' -> ChessFigure.BLACK_BISHOP
                    'r' -> ChessFigure.BLACK_ROOK
                    'q' -> ChessFigure.BLACK_QUEEN
                    'k' -> ChessFigure.BLACK_KING
                    else -> throw IllegalArgumentException()
                }
                figuresOnFields.add(ChessFigureOnField(figure, ChessField(rank = i, file = fileNumber)))
                fileNumber++
            }
        }
        fileNumber = 0
    }
    return figuresOnFields
}

private fun String.toSide() = when (this) {
    "w" -> ChessSide.WHITE
    "b" -> ChessSide.BLACK
    else -> throw IllegalArgumentException()
}

private fun String.toCastlings(): HashSet<ChessCastling> {
    val castlings = HashSet<ChessCastling>()
    forEach {
        val castling = when (it) {
            'K' -> ChessCastling.WHITE_KINGSIDE
            'Q' -> ChessCastling.WHITE_QUEENSIDE
            'k' -> ChessCastling.BLACK_KINGSIDE
            'q' -> ChessCastling.BLACK_QUEENSIDE
            else -> throw IllegalArgumentException()
        }
        castlings.add(castling)
    }
    return castlings
}