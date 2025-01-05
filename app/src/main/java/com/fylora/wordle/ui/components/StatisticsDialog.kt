package com.fylora.wordle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fylora.wordle.GameConstants.MAX_GUESSES
import com.fylora.wordle.data.entity.GameStats
import com.fylora.wordle.ui.theme.CorrectSpotColor
import com.fylora.wordle.ui.theme.NotInWordColor
import com.fylora.wordle.ui.theme.StatsBackground

@Composable
fun StatisticsDialog(
    showDialog: Boolean,
    gameStats: GameStats,
    currentGuess: Int,
    onPlayAgain: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            containerColor = StatsBackground,
            onDismissRequest = { },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Statistics",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        DataColumn(data = (gameStats.wins + gameStats.loses).toString(), name = "Played")
                        DataColumn(data = gameStats.wins.toString(), name = "Wins")
                        DataColumn(data = gameStats.loses.toString(), name = "Loses")
                    }

                    Text(
                        text = "Guesses",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Column {
                        for (i in 1..MAX_GUESSES) {
                            GuessDistributionBar(
                                guess = i,
                                count = gameStats.winsAtGuess[i]!!,
                                isCurrent = currentGuess == i
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Button(
                            onClick = {
                                onPlayAgain()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CorrectSpotColor)
                        ) {
                            Text(
                                text = "PLAY AGAIN!",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}

@Composable
fun DataColumn(
    data: String,
    name: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = data,
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = name,
            color = Color.White
        )
    }
}

@Composable
fun GuessDistributionBar(
    guess: Int,
    count: Int,
    isCurrent: Boolean = false
) {
    val barColor = if (isCurrent) CorrectSpotColor else NotInWordColor

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = "$guess",
            modifier = Modifier.padding(end = 8.dp),
            color = Color.White
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(barColor)
                .weight((count + 1).toFloat())
        )
        Text(
            text = "$count",
            color = Color.White,
            modifier = Modifier
                .weight(maxOf((MAX_GUESSES - count).toFloat(), 1f))
                .padding(horizontal = 2.dp)
        )
    }
}