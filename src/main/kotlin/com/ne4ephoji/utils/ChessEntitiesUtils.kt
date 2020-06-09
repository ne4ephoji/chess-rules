package com.ne4ephoji.utils

import com.ne4ephoji.entities.ChessField
import com.ne4ephoji.entities.ChessFigure

operator fun Array<Array<ChessFigure?>>.get(field: ChessField) = this[field.rank][field.file]