package com.xyecos.hackathon.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenHeader(
    title: String = "Список станций",
    onClick: () -> Unit,
    isWarning: Boolean = false,
    isLoading: Boolean = true,
    firstStr: String? = null,
    secondStr: String? = null,
    thirdStr: String? = null,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF),
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = RoundedCornerShape(0.dp)
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
                        .defaultMinSize(minHeight = 48.dp),
                    color = Color.Black,
                    text = title,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = W600,
                        textAlign = TextAlign.Center
                    ),
                )
                if (!isLoading) {
                    firstStr?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                )
                        )
                    }

                    secondStr?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                )
                        )
                    }

                    thirdStr?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(
                                    top = 4.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                )
                        )
                    }
                }
            }
            if (!isLoading) {
                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.End,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 24.dp,
                            )
                            .size(16.dp)
                            .background(
                                Color(
                                    if (isWarning) {
                                        0xfff57f29
                                    } else {
                                        0xFF4CAF50
                                    }
                                ),
                                CircleShape
                            ),
                    )
                }
            }
        }
    }

}

@Composable
@Preview
fun previewAppBar() {
    ScreenHeader(onClick = {})
}