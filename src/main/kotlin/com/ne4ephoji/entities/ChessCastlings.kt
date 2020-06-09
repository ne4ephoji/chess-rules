package com.ne4ephoji.entities

data class ChessCastlings(
    val whiteKingsideCastlingAvailable: Boolean,
    val whiteQueensideCastlingAvailable: Boolean,
    val blackKingsideCastlingAvailable: Boolean,
    val blackQueensideCastlingAvailable: Boolean
)