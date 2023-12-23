package com.xyecos.hackathon.presentation.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.ui.theme.HackathonTheme
import com.xyecos.hackathon.ui.theme.bold
import com.xyecos.hackathon.ui.theme.mainBlue

@Composable
fun LoginFormFilledButton(
    modifier: Modifier = Modifier,
    text: String = "Войти",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = mainBlue
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text.toUpperCase(),
            modifier = Modifier,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = bold,
                fontSize = 14.sp
            ),
        )
    }
}

@Preview
@Composable
fun LoginFormButtonPreview() {
    HackathonTheme {
        LoginFormFilledButton()
    }
}