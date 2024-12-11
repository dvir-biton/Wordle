package com.fylora.wordle.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.fylora.wordle.GameConstants.MAX_GUESSES
import com.fylora.wordle.GameConstants.MAX_LETTERS_IN_WORD
import com.fylora.wordle.data.entity.GameStats

data class WordleState(
    val guesses: SnapshotStateList<Word> = mutableStateListOf<Word>().apply {
        repeat(MAX_GUESSES) { add(Word()) }
    },
    val gameState: GameState = GameState.Running,
    val isDialogShowing: Boolean = false,
    val isAnimating: Boolean = false,
    val gameStats: GameStats = GameStats()
)

sealed interface GameState {
    data object Running: GameState
    data object Won: GameState
    data object Lost: GameState
}

data class Character(
    val char: Char,
    val type: LetterType
)

data class Word(
    val characters: SnapshotStateList<Character> = mutableStateListOf<Character>().apply {
        repeat(MAX_LETTERS_IN_WORD) { add(Character(' ', LetterType.NotSubmitted)) }
    }
) {
    override fun toString(): String {
        return characters.map { it.char }.joinToString("")
    }
}

