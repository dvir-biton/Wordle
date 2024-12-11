package com.fylora.wordle.ui

sealed interface WordleEvent {
    data class OnEnterCharacter(val character: Char): WordleEvent
    data object OnDeleteCharacter: WordleEvent
    data object OnSubmitWord: WordleEvent
    data object OnPlayAgain: WordleEvent
}