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

private fun String.toChessFieldOrNull(): ChessField? {
    return if (length != 2) {
        if (this == "-") null else throw ChessException("Wrong chess field: ${this}!")
    } else {
        toChessField()
    }
}

private fun String.toChessField(): ChessField {
    return if (length != 2) {
        throw ChessException("Wrong chess field: ${this}!")
    } else {
        ChessField(rank = this[1].toRankNumber(), file = this[0].toFileNumber())
    }
}

private fun ChessField.convertToString(): String {
    return file.toCharAsFileNumber().toString() + rank.toCharAsRankNumber()
}

private fun Char.toFileNumber(): Int {
    val fileNumberInAscii = toInt()
    return if (fileNumberInAscii in 97..122) {
        fileNumberInAscii - 97
    } else throw ChessException("Wrong file: $this!")
}

private fun Char.toRankNumber(): Int {
    return if (isDigit()) {
        val value = CHESSBOARD_SIZE - toString().toInt()
        if (value > CHESSBOARD_SIZE) throw ChessException("Wrong rank: $this") else value
    } else throw ChessException("Wrong rank: $this")
}

private fun Int.toCharAsRankNumber(): Char {
    return (CHESSBOARD_SIZE - this).toString()[0]
}

private fun Int.toCharAsFileNumber(): Char {
    return toChar() + 97
}

fun String.toChessMove(position: ChessPosition): ChessMove {
    val trimmedLength = if (endsWith('+') || endsWith('#')) length - 1 else length
    val trimmedMove = substring(0, trimmedLength)
    if (trimmedMove.contains('-')) {
        val sourceField = when (position.sideToMove) {
            ChessSide.WHITE -> ChessField(rank = 7, file = 4)
            ChessSide.BLACK -> ChessField(rank = 0, file = 4)
        }
        val targetField = when (trimmedMove.length) {
            5 -> ChessField(rank = sourceField.rank, file = sourceField.file - 2)
            3 -> ChessField(rank = sourceField.rank, file = sourceField.file + 2)
            else -> throw ChessException("Wrong castling: $this!")
        }
        return ChessMove.Castling(sourceField, targetField)
    } else if (trimmedMove.contains('=')) {
        val transformationFigureType = trimmedMove.last().toFigureType()
        val transformationFigure = ChessFigure(transformationFigureType, position.sideToMove)
        val trimmedTransformation = trimmedMove.substring(0, trimmedMove.length - 2)
        val sourceRank = when (position.sideToMove) {
            ChessSide.WHITE -> 1
            ChessSide.BLACK -> 6
        }
        val sourceFile = trimmedMove.first().toFileNumber()
        val sourceField = ChessField(rank = sourceRank, file = sourceFile)
        val targetField = trimmedTransformation.substring(
            trimmedTransformation.length - 2,
            trimmedTransformation.length
        ).toChessField()
        return if (trimmedMove.contains('x')) {
            ChessMove.TransformationTake(sourceField, targetField, transformationFigure)
        } else {
            ChessMove.TransformationMovement(sourceField, targetField, transformationFigure)
        }
    } else {
        val availableMoves = position.getAvailableMoves()
        val targetField = trimmedMove.substring(trimmedMove.length - 2, trimmedMove.length).toChessField()
        val firstChar = trimmedMove.first()
        if (firstChar.isUpperCase()) {
            val figureType = firstChar.toFigureType()
            val possibleMoves = availableMoves.filter {
                position.figures[it.source]?.type == figureType && it.target == targetField
            }
            return if (possibleMoves.size == 1) {
                possibleMoves.first()
            } else {
                if (trimmedMove[1].isDigit()) {
                    val sourceRank = trimmedMove[1].toRankNumber()
                    possibleMoves.first { it.source.rank == sourceRank }
                } else {
                    val sourceFile = trimmedMove[1].toFileNumber()
                    possibleMoves.first { it.source.file == sourceFile }
                }
            }
        } else {
            val sourceFileNumber = firstChar.toFileNumber()
            return availableMoves.first {
                it.target == targetField
                        && it.source.file == sourceFileNumber
                        && position.figures[it.source]?.type == ChessFigure.Type.PAWN
            }
        }
    }
}

fun ChessMove.toString(position: ChessPosition): String {
    return when (this) {
        is ChessMove.Castling -> {
            if (source.file < target.file) "0-0" else "0-0-0"
        }
        is ChessMove.EnPassantTake -> {
            source.file.toCharAsFileNumber().toString() + 'x' + target.convertToString()
        }
        is ChessMove.Movement -> {
            val sameFigureMoves = position.getAvailableMoves().filter {
                it.target == target && position.figures[it.source] == position.figures[source]
            }
            val figureType = position.figures[source]?.type
            var moveString = if (figureType != ChessFigure.Type.PAWN) {
                figureType?.toChar().toString()
            } else ""
            if (sameFigureMoves.size == 2) {
                moveString += if (sameFigureMoves.first().source.file != sameFigureMoves.first().source.file) {
                    source.file.toCharAsFileNumber()
                } else {
                    source.rank.toCharAsRankNumber()
                }
            }
            moveString += target.convertToString()
            moveString
        }
        is ChessMove.Take -> {
            val sameFigureMoves = position.getAvailableMoves().filter {
                it.target == target && position.figures[it.source] == position.figures[source]
            }
            val figureType = position.figures[source]?.type
            var moveString = if (figureType != ChessFigure.Type.PAWN) {
                figureType?.toChar().toString()
            } else ""
            if (sameFigureMoves.size == 2) {
                moveString += if (sameFigureMoves.first().source.file != sameFigureMoves.first().source.file) {
                    source.file.toCharAsFileNumber()
                } else {
                    source.rank.toCharAsRankNumber()
                }
            }
            moveString += 'x'
            moveString += target.convertToString()
            moveString
        }
        is ChessMove.TransformationMovement -> return target.convertToString() + '=' + figure.type.toChar()
        is ChessMove.TransformationTake -> return source.file.toCharAsFileNumber().toString() +
                'x' +
                target.convertToString() +
                '=' +
                figure.type.toChar()
    }
}

private fun Char.toFigureType(): ChessFigure.Type {
    return when (this) {
        'N' -> ChessFigure.Type.KNIGHT
        'B' -> ChessFigure.Type.BISHOP
        'R' -> ChessFigure.Type.ROOK
        'Q' -> ChessFigure.Type.QUEEN
        'K' -> ChessFigure.Type.KING
        else -> throw ChessException("Wrong figure type: $this!")
    }
}

private fun ChessFigure.Type.toChar(): Char {
    return when (this) {
        ChessFigure.Type.KNIGHT -> 'N'
        ChessFigure.Type.BISHOP -> 'B'
        ChessFigure.Type.ROOK -> 'R'
        ChessFigure.Type.QUEEN -> 'Q'
        ChessFigure.Type.KING -> 'K'
        else -> throw ChessException("Invalid figure type for conversion: $this")
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