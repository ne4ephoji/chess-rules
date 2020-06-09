package com.ne4ephoji.utils

import com.ne4ephoji.entities.*
import com.ne4ephoji.entities.ChessPosition.Companion.CHESSBOARD_SIZE
import kotlin.math.abs

fun ChessPosition.getAvailableMoves(): Set<ChessMove> {
    val movesList = mutableSetOf<ChessMove>()
    for (i in 0 until CHESSBOARD_SIZE) {
        for (j in 0 until CHESSBOARD_SIZE) {
            movesList += getAvailableMovesFromField(ChessField(rank = i, file = j))
        }
    }
    return movesList.toSet()
}

fun ChessPosition.getAvailableMovesFromField(field: ChessField): Set<ChessMove> {
    val figure = figures[field]
    return if (figure == null || figure.side != sideToMove) emptySet() else {
        when (figure.type) {
            ChessFigure.Type.PAWN -> getPawnMovesFromField(field)
            ChessFigure.Type.KNIGHT -> getKnightMovesFromField(field)
            ChessFigure.Type.BISHOP -> getBishopMovesFromField(field)
            ChessFigure.Type.ROOK -> getRookMovesFromField(field)
            ChessFigure.Type.QUEEN -> getQueenMovesFromField(field)
            ChessFigure.Type.KING -> getKingMovesFromField(field)
        }
    }
}

private fun ChessPosition.getPawnMovesFromField(field: ChessField): Set<ChessMove> {
    val moves = mutableSetOf<ChessMove>()

    when (sideToMove) {
        ChessSide.WHITE -> {
            if (field.rank == 1) {
                if (figures[field.rank - 1][field.file] == null) {
                    val targetField = ChessField(rank = field.rank - 1, file = field.file)
                    moves += createTransformationMovementMoves(field, targetField, sideToMove)
                }
                if (field.file - 1 >= 0 && figures[field.rank - 1][field.file - 1]?.side == ChessSide.BLACK) {
                    val targetField = ChessField(rank = field.rank - 1, file = field.file - 1)
                    moves += createTransformationTakeMoves(field, targetField, sideToMove)
                }
                if (field.file + 1 < CHESSBOARD_SIZE && figures[field.rank - 1][field.file + 1]?.side == ChessSide.BLACK) {
                    val targetField = ChessField(rank = field.rank - 1, file = field.file + 1)
                    moves += createTransformationTakeMoves(field, targetField, sideToMove)
                }
            } else {
                when (field.rank) {
                    3 -> enPassantTarget?.let {
                        if (abs(it.file - field.file) == 1) {
                            moves.add(ChessMove.EnPassantTake(field, ChessField(rank = it.rank - 1, file = it.file)))
                        }
                    }
                    6 -> if (figures[field.rank - 1][field.file] == null && figures[field.rank - 2][field.file] == null) {
                        moves.add(ChessMove.Movement(field, ChessField(rank = field.rank - 2, file = field.file)))
                    }
                }
                if (figures[field.rank - 1][field.file] == null) {
                    moves.add(ChessMove.Movement(field, ChessField(rank = field.rank - 1, file = field.file)))
                }
                if (field.file - 1 >= 0 && figures[field.rank - 1][field.file - 1]?.side == ChessSide.BLACK) {
                    moves.add(ChessMove.Take(field, ChessField(rank = field.rank - 1, file = field.file - 1)))
                }
                if (field.file + 1 < CHESSBOARD_SIZE && figures[field.rank - 1][field.file + 1]?.side == ChessSide.BLACK) {
                    moves.add(ChessMove.Take(field, ChessField(rank = field.rank - 1, file = field.file + 1)))
                }
            }
        }
        ChessSide.BLACK -> {
            if (field.rank == 6) {
                if (figures[field.rank + 1][field.file] == null) {
                    val targetField = ChessField(rank = field.rank + 1, file = field.file)
                    moves += createTransformationMovementMoves(field, targetField, sideToMove)
                }
                if (field.file - 1 >= 0 && figures[field.rank + 1][field.file - 1]?.side == ChessSide.WHITE) {
                    val targetField = ChessField(rank = field.rank + 1, file = field.file - 1)
                    moves += createTransformationTakeMoves(field, targetField, sideToMove)
                }
                if (field.file + 1 < CHESSBOARD_SIZE && figures[field.rank + 1][field.file + 1]?.side == ChessSide.WHITE) {
                    val targetField = ChessField(rank = field.rank + 1, file = field.file + 1)
                    moves += createTransformationTakeMoves(field, targetField, sideToMove)
                }
            } else {
                when (field.rank) {
                    4 -> enPassantTarget?.let {
                        if (abs(it.file - field.file) == 1) {
                            moves.add(ChessMove.EnPassantTake(field, ChessField(rank = it.rank + 1, file = it.file)))
                        }
                    }
                    1 -> if (figures[field.rank + 1][field.file] == null && figures[field.rank + 2][field.file] == null) {
                        moves.add(ChessMove.Movement(field, ChessField(rank = field.rank + 2, file = field.file)))
                    }
                }
                if (figures[field.rank + 1][field.file] == null) {
                    moves.add(ChessMove.Movement(field, ChessField(rank = field.rank + 1, file = field.file)))
                }
                if (field.file - 1 >= 0 && figures[field.rank + 1][field.file - 1]?.side == ChessSide.WHITE) {
                    moves.add(ChessMove.Take(field, ChessField(rank = field.rank + 1, file = field.file - 1)))
                }
                if (field.file + 1 < CHESSBOARD_SIZE && figures[field.rank + 1][field.file + 1]?.side == ChessSide.WHITE) {
                    moves.add(ChessMove.Take(field, ChessField(rank = field.rank + 1, file = field.file + 1)))
                }
            }
        }
    }

    return moves.toSet()
}

private fun ChessPosition.getKnightMovesFromField(field: ChessField): Set<ChessMove> {
    val moves = mutableSetOf<ChessMove>()

    if (field.rank + 2 < CHESSBOARD_SIZE) {
        if (field.file - 1 >= 0) {
            getFigureMove(field, ChessField(rank = field.rank + 2, file = field.file - 1))?.let(moves::add)
        }
        if (field.file + 1 < CHESSBOARD_SIZE) {
            getFigureMove(field, ChessField(rank = field.rank + 2, file = field.file + 1))?.let(moves::add)
        }
    }

    if (field.rank + 1 < CHESSBOARD_SIZE) {
        if (field.file - 2 >= 0) {
            getFigureMove(field, ChessField(rank = field.rank + 1, file = field.file - 2))?.let(moves::add)
        }
        if (field.file + 2 < CHESSBOARD_SIZE) {
            getFigureMove(field, ChessField(rank = field.rank + 1, file = field.file + 2))?.let(moves::add)
        }
    }

    if (field.rank - 1 >= 0) {
        if (field.file - 2 >= 0) {
            getFigureMove(field, ChessField(rank = field.rank - 1, file = field.file - 2))?.let(moves::add)
        }
        if (field.file + 2 < CHESSBOARD_SIZE) {
            getFigureMove(field, ChessField(rank = field.rank - 1, file = field.file + 2))?.let(moves::add)
        }
    }

    if (field.rank - 2 >= 0) {
        if (field.file - 1 >= 0) {
            getFigureMove(field, ChessField(rank = field.rank - 2, file = field.file - 1))?.let(moves::add)
        }
        if (field.file + 1 < CHESSBOARD_SIZE) {
            getFigureMove(field, ChessField(rank = field.rank - 2, file = field.file + 1))?.let(moves::add)
        }
    }

    return moves.toSet()
}

private fun ChessPosition.getBishopMovesFromField(field: ChessField): Set<ChessMove> {
    val moves = mutableSetOf<ChessMove>()

    var rankCounter = field.rank - 1
    var fileCounter = field.file - 1
    while (rankCounter >= 0 && fileCounter >= 0) {
        val move = getFigureMove(field, ChessField(rank = rankCounter, file = fileCounter)).apply {
            this?.let(moves::add)
        }
        if (move !is ChessMove.Movement) {
            break
        }
        rankCounter--
        fileCounter--
    }

    rankCounter = field.rank - 1
    fileCounter = field.file + 1
    while (rankCounter >= 0 && fileCounter < CHESSBOARD_SIZE) {
        val move = getFigureMove(field, ChessField(rank = rankCounter, file = fileCounter)).apply {
            this?.let(moves::add)
        }
        if (move !is ChessMove.Movement) {
            break
        }
        rankCounter--
        fileCounter++
    }

    rankCounter = field.rank + 1
    fileCounter = field.file - 1
    while (rankCounter < CHESSBOARD_SIZE && fileCounter >= 0) {
        val move = getFigureMove(field, ChessField(rank = rankCounter, file = fileCounter)).apply {
            this?.let(moves::add)
        }
        if (move !is ChessMove.Movement) {
            break
        }
        rankCounter++
        fileCounter--
    }

    rankCounter = field.rank + 1
    fileCounter = field.file + 1
    while (rankCounter < CHESSBOARD_SIZE && fileCounter < CHESSBOARD_SIZE) {
        val move = getFigureMove(field, ChessField(rank = rankCounter, file = fileCounter)).apply {
            this?.let(moves::add)
        }
        if (move !is ChessMove.Movement) {
            break
        }
        rankCounter++
        fileCounter++
    }

    return moves.toSet()
}

private fun ChessPosition.getRookMovesFromField(field: ChessField): Set<ChessMove> {
    val moves = mutableSetOf<ChessMove>()

    var fileCounter = field.file - 1
    while (fileCounter >= 0) {
        val move = getFigureMove(field, ChessField(rank = field.rank, file = fileCounter)).apply {
            this?.let(moves::add)
        }
        if (move !is ChessMove.Movement) {
            break
        }
        fileCounter--
    }

    fileCounter = field.file + 1
    while (fileCounter < CHESSBOARD_SIZE) {
        val move = getFigureMove(field, ChessField(rank = field.rank, file = fileCounter)).apply {
            this?.let(moves::add)
        }
        if (move !is ChessMove.Movement) {
            break
        }
        fileCounter++
    }

    var rankCounter = field.rank - 1
    while (rankCounter >= 0) {
        val move = getFigureMove(field, ChessField(rank = rankCounter, file = field.file)).apply {
            this?.let(moves::add)
        }
        if (move !is ChessMove.Movement) {
            break
        }
        rankCounter--
    }

    rankCounter = field.rank + 1
    while (rankCounter < CHESSBOARD_SIZE) {
        val move = getFigureMove(field, ChessField(rank = rankCounter, file = field.file)).apply {
            this?.let(moves::add)
        }
        if (move !is ChessMove.Movement) {
            break
        }
        rankCounter++
    }

    return moves.toSet()
}

private fun ChessPosition.getQueenMovesFromField(field: ChessField): Set<ChessMove> {
    return getBishopMovesFromField(field) + getRookMovesFromField(field)
}

private fun ChessPosition.getKingMovesFromField(field: ChessField): Set<ChessMove> {
    val moves = mutableSetOf<ChessMove>()

    if (field.rank - 1 >= 0) {
        if (field.file - 1 >= 0) {
            getFigureMove(field, ChessField(rank = field.rank - 1, file = field.file - 1))?.let(moves::add)
        }
        getFigureMove(field, ChessField(rank = field.rank - 1, file = field.file))?.let(moves::add)
        if (field.file + 1 < CHESSBOARD_SIZE) {
            getFigureMove(field, ChessField(rank = field.rank - 1, file = field.file + 1))?.let(moves::add)
        }
    }

    if (field.file - 1 >= 0) {
        getFigureMove(field, ChessField(rank = field.rank, file = field.file - 1))?.let(moves::add)
    }
    if (field.file + 1 < CHESSBOARD_SIZE) {
        getFigureMove(field, ChessField(rank = field.rank, file = field.file + 1))?.let(moves::add)
    }

    if (field.rank + 1 < CHESSBOARD_SIZE) {
        if (field.file - 1 >= 0) {
            getFigureMove(field, ChessField(rank = field.rank + 1, file = field.file - 1))?.let(moves::add)
        }
        getFigureMove(field, ChessField(rank = field.rank + 1, file = field.file))?.let(moves::add)
        if (field.file + 1 < CHESSBOARD_SIZE) {
            getFigureMove(field, ChessField(rank = field.rank + 1, file = field.file + 1))?.let(moves::add)
        }
    }

    when (sideToMove) {
        ChessSide.WHITE -> {
            if (field.rank == 7 && field.file == 4) {
                if (availableCastlings.whiteKingsideCastlingAvailable
                    && figures[7][5] == null
                    && figures[7][6] == null
                    && figures[7][7]?.type == ChessFigure.Type.ROOK
                    && figures[7][7]?.side == ChessSide.WHITE
                ) {
                    moves.add(ChessMove.Castling(field, ChessField(rank = 7, file = 6)))
                }
                if (availableCastlings.whiteQueensideCastlingAvailable
                    && figures[7][3] == null
                    && figures[7][2] == null
                    && figures[7][1] == null
                    && figures[7][0]?.type == ChessFigure.Type.ROOK
                    && figures[7][0]?.side == ChessSide.WHITE
                ) {
                    moves.add(ChessMove.Castling(field, ChessField(rank = 7, file = 2)))
                }
            }
        }
        ChessSide.BLACK -> {
            if (field.rank == 0 && field.file == 4) {
                if (availableCastlings.blackKingsideCastlingAvailable
                    && figures[0][5] == null
                    && figures[0][6] == null
                    && figures[0][7]?.type == ChessFigure.Type.ROOK
                    && figures[0][7]?.side == ChessSide.BLACK
                ) {
                    moves.add(ChessMove.Castling(field, ChessField(rank = 0, file = 6)))
                }
                if (availableCastlings.blackQueensideCastlingAvailable
                    && figures[0][3] == null
                    && figures[0][2] == null
                    && figures[0][1] == null
                    && figures[0][0]?.type == ChessFigure.Type.ROOK
                    && figures[0][0]?.side == ChessSide.BLACK
                ) {
                    moves.add(ChessMove.Castling(field, ChessField(rank = 0, file = 2)))
                }
            }
        }
    }

    return moves.toSet()
}

private fun ChessPosition.getFigureMove(sourceField: ChessField, targetField: ChessField): ChessMove? {
    val targetFigure = figures[targetField]
    return when {
        targetFigure == null -> ChessMove.Movement(sourceField, targetField)
        targetFigure.side != sideToMove -> ChessMove.Take(sourceField, targetField)
        else -> null
    }
}

private fun createTransformationMovementMoves(
    sourceField: ChessField,
    targetField: ChessField,
    sideToMove: ChessSide
): Set<ChessMove.TransformationMovement> {
    return setOf(
        ChessMove.TransformationMovement(
            sourceField,
            targetField,
            ChessFigure(ChessFigure.Type.KNIGHT, sideToMove)
        ),
        ChessMove.TransformationMovement(
            sourceField,
            targetField,
            ChessFigure(ChessFigure.Type.BISHOP, sideToMove)
        ),
        ChessMove.TransformationMovement(
            sourceField,
            targetField,
            ChessFigure(ChessFigure.Type.ROOK, sideToMove)
        ),
        ChessMove.TransformationMovement(
            sourceField,
            targetField,
            ChessFigure(ChessFigure.Type.QUEEN, sideToMove)
        )
    )
}

private fun createTransformationTakeMoves(
    sourceField: ChessField,
    targetField: ChessField,
    sideToMove: ChessSide
): Set<ChessMove.TransformationTake> {
    return setOf(
        ChessMove.TransformationTake(
            sourceField,
            targetField,
            ChessFigure(ChessFigure.Type.KNIGHT, sideToMove)
        ),
        ChessMove.TransformationTake(
            sourceField,
            targetField,
            ChessFigure(ChessFigure.Type.BISHOP, sideToMove)
        ),
        ChessMove.TransformationTake(
            sourceField,
            targetField,
            ChessFigure(ChessFigure.Type.ROOK, sideToMove)
        ),
        ChessMove.TransformationTake(
            sourceField,
            targetField,
            ChessFigure(ChessFigure.Type.QUEEN, sideToMove)
        )
    )
}