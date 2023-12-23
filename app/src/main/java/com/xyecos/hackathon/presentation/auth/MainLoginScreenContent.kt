package com.xyecos.hackathon.presentation.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xyecos.hackathon.R


@SuppressLint("UnrememberedMutableState")
@Composable
fun MainLoginScreenContent(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier
        .fillMaxSize()
        .paint(painterResource(id = R.drawable.logo), contentScale = ContentScale. Crop)) {

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.weight(2f)) {
            FormCard(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
            )
        }
    }
}

@Preview
@Composable
fun previewLogin(){
    MainLoginScreenContent()
}