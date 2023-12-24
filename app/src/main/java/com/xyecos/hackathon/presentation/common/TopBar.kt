package com.xyecos.hackathon.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    extraText: String? = null
) {
    Box(
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFe32213),
                        Color(0xFFf57f29),
                        Color(0xFFfcb53b),
                    )
                )
            )
            .fillMaxWidth()

    ) {
        Text(
            modifier = Modifier
                .padding(top = 32.dp, bottom = 16.dp, start = 16.dp)
                .fillMaxWidth(),
            text = "CYBER LOGIC" + if (extraText != null) " - $extraText" else "",
            color = Color.White,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Start
            ),
        )
    }
}