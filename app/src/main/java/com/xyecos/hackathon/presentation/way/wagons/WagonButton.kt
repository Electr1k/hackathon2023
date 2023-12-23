package com.xyecos.hackathon.presentation.way.wagons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xyecos.hackathon.data.dto.Wagon
import com.xyecos.hackathon.presentation.way.wagons.Owner.*

@Composable
fun WagonButton(
    modifier: Modifier = Modifier,
    wagon: Wagon,
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = modifier
            .size(200.dp, 72.dp),
        colors = ButtonDefaults.buttonColors(
            getColorForBody(wagon.owner)
        ),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(2.dp, getColorForBorder(wagon.owner))
    ) {
        androidx.compose.material3.Text(
            text = wagon.inventoryNumber.toUpperCase(),
            modifier = Modifier,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )
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