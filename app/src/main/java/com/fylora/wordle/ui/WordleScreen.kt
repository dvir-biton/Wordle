package com.fylora.wordle.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fylora.wordle.ui.components.Keyboard
import com.fylora.wordle.ui.components.Letter
import com.fylora.wordle.ui.components.StatisticsDialog
import com.fylora.wordle.ui.theme.Background
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WordleScreen(
    viewModel: WordleViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var animateRowShake by remember { mutableIntStateOf(-1) }
    var animateRowRotate by remember { mutableIntStateOf(-1) }
    var animateLetterTyping by remember { mutableStateOf(Pair(-1, -1)) }

    LaunchedEffect(key1 = true) {
        viewModel.snackbar.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.animateRowShake.collect {
            animateRowShake = it
            delay(210)
            animateRowShake = -1
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.animateRowRotate.collect {
            animateRowRotate = it
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.animateLetterTyping.collect {
            animateLetterTyping = it
            delay(20)
            animateLetterTyping = Pair(-1, -1)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "Wordle Unlimited",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                viewModel.state.guesses.forEachIndexed { index, word ->
                    ShakingRow(
                        isShaking = animateRowShake == index
                    ) {
                        word.characters.forEachIndexed { charIndex, character ->
                            Letter(
                                modifier = Modifier.size(64.dp),
                                character = character,
                                isTyped = animateLetterTyping.first == index
                                        && animateLetterTyping.second == charIndex,
                                isRotating = animateRowRotate == index,
                                animationDelay = charIndex * 300L
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(64.dp))
            Keyboard(
                getType = { viewModel.getCharState(it) },
                onClickChar = { viewModel.onEvent(WordleEvent.OnEnterCharacter(it)) },
                onDelete = { viewModel.onEvent(WordleEvent.OnDeleteCharacter) },
                onEnter = { viewModel.onEvent(WordleEvent.OnSubmitWord) },
            )
        }
    }

    StatisticsDialog(
        showDialog = viewModel.state.isDialogShowing,
        gameStats = viewModel.state.gameStats,
        currentGuess = if (viewModel.state.gameState == GameState.Won)
            viewModel.getCurrentGuessIndex() else -1,
        onPlayAgain = {
            viewModel.onEvent(
                WordleEvent.OnPlayAgain
            )
        }
    )
}

@Composable
fun ShakingRow(
    modifier: Modifier = Modifier,
    isShaking: Boolean = false,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val translation = remember { Animatable(0f) }

    LaunchedEffect(isShaking) {
        if (isShaking) {
            scope.launch {
                translation.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 210
                        0f at 0
                        25f at 30
                        (-25f) at 60
                        50f at 90
                        (-50f) at 120
                        25f at 150
                        (-25f) at 180
                        0f at 210
                    }
                )
            }
        } else {
            translation.snapTo(0f)
        }
    }

    Row(
        modifier = modifier
            .graphicsLayer {
                translationX = translation.value
            },
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        content()
    }
}