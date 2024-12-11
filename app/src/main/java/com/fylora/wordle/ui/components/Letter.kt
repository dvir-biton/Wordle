package com.fylora.wordle.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fylora.wordle.ui.Character
import com.fylora.wordle.ui.LetterType
import com.fylora.wordle.ui.theme.EmptyBorder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Letter(
    modifier: Modifier = Modifier,
    character: Character,
    fontSize: TextUnit = 32.sp,
    isTyped: Boolean,
    isRotating: Boolean,
    animationDelay: Long,
) {
    val scale = remember { Animatable(1f) }
    val rotation = remember { Animatable(0f) }
    var displayCharacter by remember { mutableStateOf(character) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = character) {
        if (!isRotating)
            displayCharacter = character
    }

    LaunchedEffect(isTyped) {
        if (isTyped) {
            scope.launch {
                scale.animateTo(
                    targetValue = 1.15f,
                    animationSpec = tween(
                        durationMillis = 10,
                        easing = FastOutLinearInEasing
                    )
                )
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 10,
                        easing = FastOutLinearInEasing
                    )
                )
            }
        }
    }

    LaunchedEffect(isRotating) {
        if (isRotating) {
            scope.launch {
                delay(animationDelay)
                rotation.animateTo(
                    targetValue = 90f,
                    animationSpec = tween(
                        durationMillis = 275,
                        easing = LinearEasing
                    )
                )
                displayCharacter = character.copy(type = character.type)
                rotation.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 275,
                        easing = LinearEasing
                    )
                )
            }
        }
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                rotationX = rotation.value
            }
            .border(
                width = 2.dp,
                color = if (character.char == ' ') EmptyBorder
                else if (displayCharacter.type == LetterType.NotSubmitted) Color.Gray
                else Color.Transparent
            )
            .background(displayCharacter.type.color),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = character.char.toString().uppercase(),
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun Letter(
    modifier: Modifier = Modifier,
    character: Character,
    fontSize: TextUnit = 32.sp,
) {
    Box(
        modifier = modifier
            .border(
                width = 2.dp,
                color = if (character.char == ' ') EmptyBorder
                else if (character.type == LetterType.NotSubmitted) Color.Gray
                else Color.Transparent
            )
            .background(character.type.color),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = character.char.toString().uppercase(),
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Black
        )
    }
}

@Preview
@Composable
fun PreviewLetter() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Letter(
            modifier = Modifier.size(50.dp),
            character = Character(
                char = 'Y',
                type = LetterType.CorrectSpot
            )
        )
        Letter(
            modifier = Modifier.size(50.dp),
            character = Character(
                char = 'A',
                type = LetterType.WrongSpot
            )
        )
        Letter(
            modifier = Modifier.size(50.dp),
            character = Character(
                char = 'B',
                type = LetterType.NotInWord
            )
        )
        Letter(
            modifier = Modifier.size(50.dp),
            character = Character(
                char = 'C',
                type = LetterType.NotSubmitted
            )
        )

        Letter(
            modifier = Modifier
                .size(30.dp, 40.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.Gray),
            character = Character(
                char = 'Y',
                type = LetterType.NotSubmitted
            ),
            fontSize = 16.sp
        )
    }
}