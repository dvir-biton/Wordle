package com.fylora.wordle.ui

import androidx.compose.ui.graphics.Color
import com.fylora.wordle.ui.theme.CorrectSpotColor
import com.fylora.wordle.ui.theme.NotInWordColor
import com.fylora.wordle.ui.theme.WrongSpotColor

sealed class LetterType(val color: Color) {
    data object CorrectSpot: LetterType(CorrectSpotColor)
    data object WrongSpot: LetterType(WrongSpotColor)
    data object NotInWord: LetterType(NotInWordColor)
    data object NotSubmitted: LetterType(Color.Transparent)
}