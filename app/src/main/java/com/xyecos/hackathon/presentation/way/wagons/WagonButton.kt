package com.xyecos.hackathon.presentation.way.wagons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.data.dto.Wagon
import com.xyecos.hackathon.presentation.way.wagons.Owner.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WagonButton(
    modifier: Modifier = Modifier,
    wagon: Wagon,
    onClick: () -> Unit,
    isSelectionMode: Boolean,
    isChecked: Boolean,
    onChangeCheck: (state: Boolean) -> Unit,
    onLongClick: () -> Unit,
    isWarning: Boolean = false,
) {
    Card(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        colors = CardDefaults.cardColors(
            containerColor = getColorForBody(wagon.owner)
        ),
        border = BorderStroke(2.dp, getColorForBorder(wagon.owner)),
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
                        ),
                    color = Color.Black,
                    text = "№ " + wagon.inventoryNumber.uppercase(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        textAlign = TextAlign.Center
                    ),
                )
                Text(
                    text = wagon.owner.name,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(
                            top = 4.dp,
                            start = 16.dp,
                            end = 16.dp,
                        )
                )

                Text(
                    text = wagon.type,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(
                            top = 4.dp,
                            start = 16.dp,
                            end = 16.dp,
                        )
                )

                Text(
                    text = wagon.operationState,
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

            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 22.dp,
                        )
                        .size(16.dp)
                        .background(Color(if (isWarning) 0xfff57f29 else 0xFF4CAF50), CircleShape),
                )
                Box(
                    modifier = Modifier, contentAlignment = Alignment.Center
                ) {
                    if (isSelectionMode) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { onChangeCheck(isChecked) })
                    }
                }
            }
        }
    }
}


fun getColorForBorder(owner: Owner): Color {
    return Color(
        when (owner) {
            HTC -> 0xFF2988AE
            GK -> 0xFF6EA566
            ATL -> 0xFFED7817
            PGK -> 0xFFB5457C
            MOD -> 0xFF8E4D9B
            RJD -> 0xFF6E6E6D
            NPK -> 0xFFFDF0EF
            FGK -> 0xFFF69112
            MECH -> 0xFF7086A9
            AGENT -> 0XFF4A8F40
            OTHER -> 0xFFB1ADC2
        },
    )
}

fun getColorForBody(owner: Owner): Color {
    return Color(
        when (owner) {
            HTC -> 0xFFBCF3FF
            GK -> 0xFFC8F4C1
            ATL -> 0xFFFFB762
            PGK -> 0xFFFFBEFC
            MOD -> 0xFFC5AAFF
            RJD -> 0xFFABABAB
            NPK -> 0xFFFCDBCB
            FGK -> 0xFFFFF3B4
            MECH -> 0xFFACCDFF
            AGENT -> 0XFF72BE7E
            OTHER -> 0xFFFFFFFF
        }
    )
}

@Preview
@Composable
fun prev() {
    WagonButton(
        wagon = Wagon(
            inventoryNumber = "123456789",
            owner = Owner.HTC,
            type = "Тип",
            cargo = "Груз",
            daysToRepair = 0,
            id = 0,
            idleDaysLength = 0,
            isDirty = false,
            isSick = false,
            isWithHatch = false,
            kilometersLeft = 0,
            loadCapacity = 0.0,
            note1 = "Примечание 1",
            note2 = "Примечание 2",
            operationState = "Состояние",
            parkId = 0,
            position = 1,
            stationId = 0,
            wayId = 1,
        ),
        onClick = {},
        isSelectionMode = true,
        isChecked = false,
        onChangeCheck = {},
        onLongClick = {},
        isWarning = false,
    )
}