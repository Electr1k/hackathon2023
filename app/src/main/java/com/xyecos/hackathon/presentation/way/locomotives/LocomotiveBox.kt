package com.xyecos.hackathon.presentation.way.locomotives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.R
import com.xyecos.hackathon.data.dto.Locomotive
import com.xyecos.hackathon.presentation.way.wagons.Direction
import com.xyecos.hackathon.presentation.way.wagons.getColorForBody
import com.xyecos.hackathon.presentation.way.wagons.getColorForBorder
import com.xyecos.hackathon.ui.theme.Locomotive
import com.xyecos.hackathon.ui.theme.mainBlue

@Composable
fun LocomotiveBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    locomotive: Locomotive
){
    Card(
        modifier = modifier
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(2.dp, Color.Black),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(3f)
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                        ),
                    color = Color.Black,
                    text = "№ " + locomotive.inventoryNumber,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center
                    ),
                )
                Text(
                    text = "Положение: " + if (locomotive.direction == Direction.LEFT) "Лево" else "Право",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(
                            top = 4.dp,
                            start = 16.dp,
                            end = 16.dp,
                        )
                )

                Box(modifier = Modifier.height(16.dp))
            }

            Icon(
                modifier = Modifier.size(45.dp),
                painter = painterResource(id = R.drawable.ic_loco),
                contentDescription = "",
                tint = mainBlue
            )
        }
    }
}

@Preview
@Composable
fun prere() {
    LocomotiveBox(
        locomotive = Locomotive(
            inventoryNumber = "123",
            direction = Direction.LEFT,
            id = 1
        ),
        onClick = {}
    )
}