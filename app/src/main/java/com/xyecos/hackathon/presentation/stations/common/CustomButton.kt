package com.xyecos.hackathon.presentation.stations.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.ui.theme.mainBlue

@Composable
fun CustomBox(
    modifier: Modifier = Modifier,
    text: String?,
    onClick: () -> Unit,
    isWarning: Boolean = true
){
    Card(
        modifier = modifier
            .clickable { onClick() }
        ,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF),
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Row {
            Column(
                modifier = Modifier.weight(3f)
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                        )
                        .defaultMinSize(minHeight = 32.dp),
                    color = Color.Black,
                    text = text.orEmpty().uppercase(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center
                    ),
                )
                Text(
                    text = "5 парков загружено",
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                        )
                )

                Text(
                    text = "5 путей загружено",
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                        )
                )

                Text(
                    text = "2 локомотива в работе",
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                        )
                )

                Text(
                    text = "14 вагонов на станции",
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                        )
                        .defaultMinSize(minHeight = 34.dp),
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 24.dp,
                        )
                        .size(16.dp)
                        .background(Color(if (isWarning) 0xfff57f29 else 0xFF4CAF50), CircleShape),
                )
            }
        }
    }
//
//    OutlinedButton(
//        onClick = onClick,
//        modifier = modifier
//            .fillMaxWidth()
//            .height(52.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.White
//
//        ),
//        border = ButtonDefaults.outlinedButtonBorder.copy(
//            width = 2.dp,
//            brush = Brush.linearGradient(
//                colors = listOf(
//                    Color(0xffe32213),
//                    Color(0xfff57f29),
//                )
//            )
//        ),
//        shape = RoundedCornerShape(4.dp)
//    ) {
//        if (text != null) {
//            Text(
//                color = Color(0xFF000000),
//                text = text.toUpperCase(),
//                fontSize = 16.sp,
//                modifier = Modifier,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
}

@Preview()
@Composable
fun previewCard() {
    CustomBox(
        text = "Новокузнецк-Северный",
        onClick = {}
    )


}