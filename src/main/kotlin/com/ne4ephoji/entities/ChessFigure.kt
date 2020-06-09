package com.ne4ephoji.entities

data class ChessFigure(val type: Type, val side: ChessSide) {
    enum class Type {
        PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
    }
}