package com.ne4ephoji.utils

import com.ne4ephoji.entities.*
import org.junit.Assert.assertEquals
import org.junit.Test

class ChessParseUtilsTest {
    @Test
    fun testStartingPositionParse() {
        val expected = ChessPosition(
            size = 8,
            figuresOnFields = hashSetOf(
                ChessFigureOnField(ChessFigure.WHITE_PAWN, ChessField(6, 0)),
                ChessFigureOnField(ChessFigure.WHITE_PAWN, ChessField(6, 1)),
                ChessFigureOnField(ChessFigure.WHITE_PAWN, ChessField(6, 2)),
                ChessFigureOnField(ChessFigure.WHITE_PAWN, ChessField(6, 3)),
                ChessFigureOnField(ChessFigure.WHITE_PAWN, ChessField(6, 4)),
                ChessFigureOnField(ChessFigure.WHITE_PAWN, ChessField(6, 5)),
                ChessFigureOnField(ChessFigure.WHITE_PAWN, ChessField(6, 6)),
                ChessFigureOnField(ChessFigure.WHITE_PAWN, ChessField(6, 7)),
                ChessFigureOnField(ChessFigure.BLACK_PAWN, ChessField(1, 0)),
                ChessFigureOnField(ChessFigure.BLACK_PAWN, ChessField(1, 1)),
                ChessFigureOnField(ChessFigure.BLACK_PAWN, ChessField(1, 2)),
                ChessFigureOnField(ChessFigure.BLACK_PAWN, ChessField(1, 3)),
                ChessFigureOnField(ChessFigure.BLACK_PAWN, ChessField(1, 4)),
                ChessFigureOnField(ChessFigure.BLACK_PAWN, ChessField(1, 5)),
                ChessFigureOnField(ChessFigure.BLACK_PAWN, ChessField(1, 6)),
                ChessFigureOnField(ChessFigure.BLACK_PAWN, ChessField(1, 7)),
                ChessFigureOnField(ChessFigure.WHITE_KNIGHT, ChessField(7, 1)),
                ChessFigureOnField(ChessFigure.WHITE_KNIGHT, ChessField(7, 6)),
                ChessFigureOnField(ChessFigure.BLACK_KNIGHT, ChessField(0, 1)),
                ChessFigureOnField(ChessFigure.BLACK_KNIGHT, ChessField(0, 6)),
                ChessFigureOnField(ChessFigure.WHITE_BISHOP, ChessField(7, 2)),
                ChessFigureOnField(ChessFigure.WHITE_BISHOP, ChessField(7, 5)),
                ChessFigureOnField(ChessFigure.BLACK_BISHOP, ChessField(0, 2)),
                ChessFigureOnField(ChessFigure.BLACK_BISHOP, ChessField(0, 5)),
                ChessFigureOnField(ChessFigure.WHITE_ROOK, ChessField(7, 0)),
                ChessFigureOnField(ChessFigure.WHITE_ROOK, ChessField(7, 7)),
                ChessFigureOnField(ChessFigure.BLACK_ROOK, ChessField(0, 0)),
                ChessFigureOnField(ChessFigure.BLACK_ROOK, ChessField(0, 7)),
                ChessFigureOnField(ChessFigure.WHITE_QUEEN, ChessField(7, 3)),
                ChessFigureOnField(ChessFigure.BLACK_QUEEN, ChessField(0, 3)),
                ChessFigureOnField(ChessFigure.WHITE_KING, ChessField(7, 4)),
                ChessFigureOnField(ChessFigure.BLACK_KING, ChessField(0, 4))
            ),
            sideToMove = ChessSide.WHITE,
            availableCastlings = hashSetOf(
                ChessCastling.WHITE_KINGSIDE,
                ChessCastling.WHITE_QUEENSIDE,
                ChessCastling.BLACK_KINGSIDE,
                ChessCastling.BLACK_QUEENSIDE
            ),
            enPassantTarget = null,
            movesSinceLastTakeOrPawnMove = 0,
            moveNumber = 1
        )
        val actual = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1".asFENToPosition()
        assertEquals(expected, actual)
    }
}