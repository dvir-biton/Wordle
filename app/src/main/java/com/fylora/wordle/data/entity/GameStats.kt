package com.fylora.wordle.data.entity

data class GameStats(
    val wins: Int = 0,
    val loses: Int = 0,
    val winsAtGuess: MutableMap<Int, Int> = mutableMapOf(
        1 to 0,
        2 to 0,
        3 to 0,
        4 to 0,
        5 to 0,
        6 to 0
    )
)
