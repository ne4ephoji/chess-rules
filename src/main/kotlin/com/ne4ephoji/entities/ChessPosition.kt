package com.ne4ephoji.entities

data class ChessPosition(
    val size: Int,
    val figuresOnFields: HashSet<ChessFigureOnField>,
    var sideToMove: ChessSide,
    val availableCastlings: HashSet<ChessCastling>,
    var enPassantTarget: ChessField?,
    var movesSinceLastTakeOrPawnMove: Int,
    var moveNumber: Int
)