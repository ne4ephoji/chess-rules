package com.ne4ephoji.entities

sealed class ChessMove(open val source: ChessField, open val target: ChessField) {
    data class Castling(
        override val source: ChessField,
        override val target: ChessField
    ) : ChessMove(source, target)

    data class EnPassantTake(
        override val source: ChessField,
        override val target: ChessField
    ) : ChessMove(source, target)

    data class Movement(
        override val source: ChessField,
        override val target: ChessField
    ) : ChessMove(source, target)

    data class Take(
        override val source: ChessField,
        override val target: ChessField
    ) : ChessMove(source, target)

    data class TransformationMovement(
        override val source: ChessField,
        override val target: ChessField,
        val figure: ChessFigure
    ) : ChessMove(source, target)

    data class TransformationTake(
        override val source: ChessField,
        override val target: ChessField,
        val figure: ChessFigure
    ) : ChessMove(source, target)
}