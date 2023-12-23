package com.xyecos.hackathon.presentation.way.wagons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    onLongClick: () -> Unit
){
    Card(
        modifier = modifier
            .size(200.dp, 72.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
        colors = CardDefaults.cardColors(
            getColorForBody(wagon.owner)
        ),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(2.dp, getColorForBorder(wagon.owner))
    ) {
        if (isSelectionMode){
            Checkbox(modifier = Modifier.align(Alignment.End), checked = isChecked, onCheckedChange = {onChangeCheck(isChecked)})
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = wagon.inventoryNumber.toUpperCase(),
                modifier = Modifier,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
        }
    }
}


fun getColorForBorder(owner: Owner): Color {
    return Color(when (owner){
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
    },  )
}

fun getColorForBody(owner: Owner): Color {
    return Color(when (owner){
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
    })
}