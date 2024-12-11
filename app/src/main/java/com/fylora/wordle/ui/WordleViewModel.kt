package com.fylora.wordle.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fylora.wordle.GameConstants.MAX_GUESSES
import com.fylora.wordle.GameConstants.MAX_LETTERS_IN_WORD
import com.fylora.wordle.data.StatisticsService
import com.fylora.wordle.data.WordDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordleViewModel @Inject constructor(
    private val dataSource: WordDataSource,
    private val statisticsService: StatisticsService,
    private val mapCharacterToStateUseCase: MapCharacterToStateUseCase,
): ViewModel() {
    var state by mutableStateOf(WordleState())
        private set

    private val _snackbar = Channel<String>()
    val snackbar = _snackbar.receiveAsFlow()

    private val _animateRowShake = Channel<Int>()
    val animateRowShake = _animateRowShake.receiveAsFlow()

    private val _animateRowRotate = Channel<Int>()
    val animateRowRotate = _animateRowRotate.receiveAsFlow()

    private val _animateLetterTyping = Channel<Pair<Int, Int>>()
    val animateLetterTyping = _animateLetterTyping.receiveAsFlow()

    private lateinit var chosenWord: String
    private var currentGuessIndex = 0
    private var currentCharacterIndex = 0
    private var isGameFinished = false

    init {
        viewModelScope.launch {
            chosenWord = dataSource.getRandomWord().uppercase()
            state = state.copy(gameStats = statisticsService.loadGameStats())
        }
    }

    fun onEvent(event: WordleEvent) {
        viewModelScope.launch {
            when (event) {
                WordleEvent.OnDeleteCharacter -> {
                    if (currentCharacterIndex == 0)
                        return@launch

                    currentCharacterIndex--

                    state.guesses[currentGuessIndex].characters[currentCharacterIndex] = Character(' ', LetterType.NotSubmitted)
                }
                is WordleEvent.OnEnterCharacter -> {
                    if (currentCharacterIndex >= MAX_LETTERS_IN_WORD || currentGuessIndex >= MAX_GUESSES)
                        return@launch

                    _animateLetterTyping.send(Pair(currentGuessIndex, currentCharacterIndex))
                    state.guesses[currentGuessIndex].characters[currentCharacterIndex] = Character(event.character, LetterType.NotSubmitted)
                    currentCharacterIndex++
                }
                WordleEvent.OnSubmitWord -> {
                    if (currentCharacterIndex != MAX_LETTERS_IN_WORD) {
                        _animateRowShake.send(currentGuessIndex)
                        _snackbar.send("Not enough letters")
                        return@launch
                    }

                    if (!dataSource.doesWordExist(state.guesses[currentGuessIndex].toString())) {
                        _animateRowShake.send(currentGuessIndex)
                        _snackbar.send("Not in word list")
                        return@launch
                    }

                    val updatedCharacters = mapCharacterToStateUseCase(state.guesses[currentGuessIndex], chosenWord)

                    val updatedGuess = Word(mutableStateListOf<Character>().apply {
                        addAll(mapCharacterToStateUseCase(state.guesses[currentGuessIndex], chosenWord))
                    })

                    _animateRowRotate.send(currentGuessIndex)
                    state.guesses[currentGuessIndex] = updatedGuess
                    currentGuessIndex++
                    currentCharacterIndex = 0

                    if (updatedCharacters.map { it.char }.joinToString("") == chosenWord) {
                        _snackbar.send("Genius")

                        val winsAtGuess = state.gameStats.winsAtGuess.toMutableMap()
                        winsAtGuess[currentGuessIndex] = winsAtGuess.getOrDefault(currentGuessIndex, 0) + 1

                        state = state.copy(
                            gameState = GameState.Won,
                            gameStats = state.gameStats.copy(
                                wins = state.gameStats.wins + 1,
                                winsAtGuess = winsAtGuess
                            )
                        )
                        isGameFinished = true
                    } else if (currentGuessIndex == MAX_GUESSES) {
                        _snackbar.send(chosenWord)
                        state = state.copy(
                            gameState = GameState.Lost,
                            gameStats = state.gameStats.copy(
                                loses = state.gameStats.loses + 1
                            )
                        )
                        isGameFinished = true
                    }
                    if (isGameFinished) {
                        statisticsService.saveGameStats(
                            gameStats = state.gameStats
                        )
                        delay(1750L)
                        state = state.copy(isDialogShowing = true)
                    }
                }
                WordleEvent.OnPlayAgain -> {
                    chosenWord = dataSource.getRandomWord().uppercase()
                    _animateRowRotate.send(-1)
                    state = WordleState(gameStats = state.gameStats)
                    isGameFinished = false
                    currentGuessIndex = 0
                    currentCharacterIndex = 0
                }
            }
        }
    }

    fun getCharState(char: Char): LetterType {
        var letterState: LetterType = LetterType.NotSubmitted

        state.guesses.forEach { word ->
            word.characters.forEach {
                if (it.char == char) {
                    when (it.type) {
                        is LetterType.CorrectSpot -> return LetterType.CorrectSpot
                        is LetterType.WrongSpot -> if (letterState is LetterType.NotSubmitted || letterState is LetterType.NotInWord)
                            letterState = LetterType.WrongSpot
                        is LetterType.NotInWord -> if (letterState is LetterType.NotSubmitted)
                            letterState = LetterType.NotInWord
                        else -> {}
                    }
                }
            }
        }

        return letterState
    }

    fun getCurrentGuessIndex() = currentGuessIndex
}