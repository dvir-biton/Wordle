package com.fylora.wordle.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    action: () -> Unit
) {
    Box(
        modifier = modifier
            .size(64.dp, 48.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(Color.Gray)
            .clickable { action() },
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text.uppercase(),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    action: () -> Unit
) {
    Box(
        modifier = modifier
            .size(64.dp, 48.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(Color.Gray)
            .clickable { action() },
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = icon,
            tint = Color.White,
            contentDescription = null
        )
    }
}