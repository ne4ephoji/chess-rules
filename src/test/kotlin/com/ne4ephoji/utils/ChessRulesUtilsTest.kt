package com.ne4ephoji.utils

import com.ne4ephoji.entities.ChessField
import com.ne4ephoji.entities.ChessFigure
import com.ne4ephoji.entities.ChessMove
import com.ne4ephoji.entities.ChessSide
import org.junit.Assert
import org.junit.Test

class ChessRulesUtilsTest {
    @Test
    fun testPawnMovementMoves() {
        val position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1".asFENToPosition()
        val expectedMoves = setOf(
            ChessMove.Movement(ChessField(6, 4), ChessField(5, 4)),
            ChessMove.Movement(ChessField(6, 4), ChessField(4, 4))
        )
        val actualMoves = position.getAvailableMovesFromField(ChessField(6, 4))
        Assert.assertEquals(expectedMoves, actualMoves)
    }

    @Test
    fun testPawnTakeMoves() {
        val position = "rnbqkbnr/ppp1p1pp/8/8/8/3p1p2/PPPPPPPP/RNBQKBNR w KQkq - 0 1".asFENToPosition()
        val expectedMoves = setOf(
            ChessMove.Movement(ChessField(6, 4), ChessField(5, 4)),
            ChessMove.Movement(ChessField(6, 4), ChessField(4, 4)),
            ChessMove.Take(ChessField(6, 4), ChessField(5, 3)),
            ChessMove.Take(ChessField(6, 4), ChessField(5, 5))
        )
        val actualMoves = position.getAvailableMovesFromField(ChessField(6, 4))
        Assert.assertEquals(expectedMoves, actualMoves)
    }

    @Test
    fun testPawnEnPassantMoves() {
        val position = "rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPP1PPP/RNBQKBNR w KQkq d5 0 1".asFENToPosition()
        val expectedMoves = setOf(
            ChessMove.Movement(ChessField(3, 4), ChessField(2, 4)),
            ChessMove.EnPassantTake(ChessField(3, 4), ChessField(2, 3))
        )
        val actualMoves = position.getAvailableMovesFromField(ChessField(3, 4))
        Assert.assertEquals(expectedMoves, actualMoves)
    }

    @Test
    fun testPawnTransformationMoves() {
        val position = "r2qkbnr/pbPppppp/2n5/1p6/8/8/PPP1PPPP/RNBQKBNR w KQkq - 0 1".asFENToPosition()
        val expectedMoves = setOf(
            ChessMove.TransformationMovement(
                ChessField(1, 2),
                ChessField(0, 2),
                ChessFigure(ChessFigure.Type.KNIGHT, ChessSide.WHITE)
            ),
            ChessMove.TransformationMovement(
                ChessField(1, 2),
                ChessField(0, 2),
                ChessFigure(ChessFigure.Type.BISHOP, ChessSide.WHITE)
            ),
            ChessMove.TransformationMovement(
                ChessField(1, 2),
                ChessField(0, 2),
                ChessFigure(ChessFigure.Type.ROOK, ChessSide.WHITE)
            ),
            ChessMove.TransformationMovement(
                ChessField(1, 2),
                ChessField(0, 2),
                ChessFigure(ChessFigure.Type.QUEEN, ChessSide.WHITE)
            ),
            ChessMove.TransformationTake(
                ChessField(1, 2),
                ChessField(0, 3),
                ChessFigure(ChessFigure.Type.KNIGHT, ChessSide.WHITE)
            ),
            ChessMove.TransformationTake(
                ChessField(1, 2),
                ChessField(0, 3),
                ChessFigure(ChessFigure.Type.BISHOP, ChessSide.WHITE)
            ),
            ChessMove.TransformationTake(
                ChessField(1, 2),
                ChessField(0, 3),
                ChessFigure(ChessFigure.Type.ROOK, ChessSide.WHITE)
            ),
            ChessMove.TransformationTake(
                ChessField(1, 2),
                ChessField(0, 3),
                ChessFigure(ChessFigure.Type.QUEEN, ChessSide.WHITE)
            )
        )
        val actualMoves = position.getAvailableMovesFromField(ChessField(1, 2))
        Assert.assertEquals(expectedMoves, actualMoves)
    }

    @Test
    fun testKnightMoves() {
        val position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1".asFENToPosition()
        val expectedMoves = setOf(
            ChessMove.Movement(ChessField(7, 6), ChessField(5, 5)),
            ChessMove.Movement(ChessField(7, 6), ChessField(5, 7))
        )
        val actualMoves = position.getAvailableMovesFromField(ChessField(7, 6))
        Assert.assertEquals(expectedMoves, actualMoves)
    }

    @Test
    fun testBishopMoves() {
        val position = "rnbqkbnr/p1pppppp/8/1p6/8/6PP/PPPP1P2/RNBQKBNR w KQkq - 0 1".asFENToPosition()
        val expectedMoves = setOf(
            ChessMove.Movement(ChessField(7, 5), ChessField(6, 4)),
            ChessMove.Movement(ChessField(7, 5), ChessField(5, 3)),
            ChessMove.Movement(ChessField(7, 5), ChessField(4, 2)),
            ChessMove.Take(ChessField(7, 5), ChessField(3, 1)),
            ChessMove.Movement(ChessField(7, 5), ChessField(6, 6))
        )
        val actualMoves = position.getAvailableMovesFromField(ChessField(7, 5))
        Assert.assertEquals(expectedMoves, actualMoves)
    }

    @Test
    fun testRookMoves() {
        val position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPP1/RNBQK2R w KQkq - 0 1".asFENToPosition()
        val expectedMoves = setOf(
            ChessMove.Movement(ChessField(7, 7), ChessField(6, 7)),
            ChessMove.Movement(ChessField(7, 7), ChessField(5, 7)),
            ChessMove.Movement(ChessField(7, 7), ChessField(4, 7)),
            ChessMove.Movement(ChessField(7, 7), ChessField(3, 7)),
            ChessMove.Movement(ChessField(7, 7), ChessField(2, 7)),
            ChessMove.Take(ChessField(7, 7), ChessField(1, 7)),
            ChessMove.Movement(ChessField(7, 7), ChessField(7, 6)),
            ChessMove.Movement(ChessField(7, 7), ChessField(7, 5))
        )
        val actualMoves = position.getAvailableMovesFromField(ChessField(7, 7))
        Assert.assertEquals(expectedMoves, actualMoves)
    }

    @Test
    fun testQueenMoves() {
        val position = "rnbqkbnr/pppppp1p/8/8/2PPP1p1/8/PP3PPP/RNBQKBNR w KQkq - 0 1".asFENToPosition()
        val expectedMoves = setOf(
            ChessMove.Movement(ChessField(7, 3), ChessField(4, 0)),
            ChessMove.Movement(ChessField(7, 3), ChessField(5, 1)),
            ChessMove.Movement(ChessField(7, 3), ChessField(6, 2)),
            ChessMove.Movement(ChessField(7, 3), ChessField(5, 3)),
            ChessMove.Movement(ChessField(7, 3), ChessField(6, 3)),
            ChessMove.Movement(ChessField(7, 3), ChessField(5, 5)),
            ChessMove.Movement(ChessField(7, 3), ChessField(6, 4)),
            ChessMove.Take(ChessField(7, 3), ChessField(4, 6))
        )
        val actualMoves = position.getAvailableMovesFromField(ChessField(7, 3))
        Assert.assertEquals(expectedMoves, actualMoves)
    }

    @Test
    fun testKingMoves() {
        val position = "rnbqkbnr/pppp1ppp/8/8/8/8/PPP1p1PP/RNBQK2R w KQkq - 0 1".asFENToPosition()
        val expectedMoves = setOf(
            ChessMove.Movement(ChessField(7, 4), ChessField(6, 3)),
            ChessMove.Take(ChessField(7, 4), ChessField(6, 4)),
            ChessMove.Movement(ChessField(7, 4), ChessField(6, 5)),
            ChessMove.Movement(ChessField(7, 4), ChessField(7, 5)),
            ChessMove.Castling(ChessField(7, 4), ChessField(7, 6))
        )
        val actualMoves = position.getAvailableMovesFromField(ChessField(7, 4))
        Assert.assertEquals(expectedMoves, actualMoves)
    }
}