package com.ne4ephoji.entities

data class ChessCastlings(
    var whiteKingsideCastlingAvailable: Boolean,
    var whiteQueensideCastlingAvailable: Boolean,
    var blackKingsideCastlingAvailable: Boolean,
    var blackQueensideCastlingAvailable: Boolean
)