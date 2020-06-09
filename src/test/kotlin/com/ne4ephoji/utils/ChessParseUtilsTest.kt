package com.ne4ephoji.utils

import com.ne4ephoji.entities.ChessField
import com.ne4ephoji.entities.ChessMove
import org.junit.Assert.assertEquals
import org.junit.Test

class ChessParseUtilsTest {
    @Test
    fun testCastlingParsing() {
        val position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1".asFENToPosition()
        val expectedMove = ChessMove.Castling(ChessField(rank = 7, file = 4), ChessField(rank = 7, file = 2))
        val actualMove = "0-0-0".toChessMove(position)
        assertEquals(expectedMove, actualMove)
    }
}