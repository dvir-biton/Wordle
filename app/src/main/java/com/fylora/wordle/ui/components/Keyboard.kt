package com.fylora.wordle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fylora.wordle.ui.Character
import com.fylora.wordle.ui.LetterType

@Composable
fun Keyboard(
    getType: (char: Char) -> LetterType,
    onClickChar: (char: Char) -> Unit,
    onDelete: () -> Unit,
    onEnter: () -> Unit
) {
    val keyboardLayout = listOf(
        listOf('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'),
        listOf('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'),
        listOf('+', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '-')
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        keyboardLayout.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                row.forEach { char ->
                    when (char) {
                        '+' -> ActionButton(
                            text = "Enter",
                            action = { onEnter() }
                        )
                        '-' ->
                            ActionButton(
                                icon = Icons.AutoMirrored.Filled.Backspace,
                                action = { onDelete() }
                            )
                        else -> {
                            val type = getType(char)
                            Letter(
                                modifier = Modifier
                                    .size(32.dp, 48.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        if (type is LetterType.NotSubmitted)
                                            Color.Gray else type.color
                                    )
                                    .clickable { onClickChar(char) },
                                character = Character(
                                    char = char,
                                    type = type
                                ),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}