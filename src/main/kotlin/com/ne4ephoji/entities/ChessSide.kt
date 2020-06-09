package com.ne4ephoji.entities

enum class ChessSide {
    WHITE, BLACK;

    val opposite: ChessSide
        get() = when (this) {
            WHITE -> BLACK
            BLACK -> WHITE
        }
}