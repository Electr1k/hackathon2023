package com.xyecos.hackathon.presentation.stations.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.ui.theme.Gray

@Composable
fun StationCard(
    title: String? ,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        border = BorderStroke(3.dp, Gray)
    ) {
        if (title != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                textAlign = TextAlign.Center,
                text = title!!,
                fontWeight = W500,
                fontSize = 22.sp,
            )
        }else{
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Gray)
                    .padding(vertical = 18.dp),
                textAlign = TextAlign.Center,
                text = "",
                fontWeight = W500,
                fontSize = 22.sp,
            )
        }
    }
}

@Preview()
@Composable
fun previewCard() {
    StationCard(
        title = "Новокузнецк-Северный",
        onClick = {}
    )
}