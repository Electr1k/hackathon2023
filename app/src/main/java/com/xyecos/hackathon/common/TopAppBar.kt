package com.xyecos.hackathon.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopAppBar(
    title: String? = null,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .defaultMinSize(minHeight = 30.dp)){


        Row(
            modifier = Modifier.padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 14.dp,
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (title != null)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            fontWeight = W600,
                            fontSize = 22.sp,
                        ),
                        textAlign = TextAlign.Center,
                        text = title
                    )
            }
        }
        Divider(color = Color.Gray, thickness = 2.dp)
    }
}

@Composable
@Preview
fun previewAppBarr(){
    TopAppBar("AppBar")
}