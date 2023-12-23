package com.xyecos.hackathon.presentation.stations.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xyecos.hackathon.ui.theme.mainBlue

@Composable
fun CustomBox(
    modifier: Modifier = Modifier,
    text: String?,
    onClick: () -> Unit
){

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
        if (text != null) {
            Text(
                text = text.toUpperCase(),
                modifier = Modifier,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview()
@Composable
fun previewCard() {
    CustomBox(
        text = "Новокузнецк-Северный",
        onClick = {}
    )


}