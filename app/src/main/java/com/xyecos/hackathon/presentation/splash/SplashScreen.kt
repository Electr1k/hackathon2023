package com.xyecos.hackathon.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xyecos.hackathon.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    openApp: () -> Unit
){

    val visible = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        visible.value = true
        delay(700)
        openApp()
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(vertical = 40.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        AnimatedVisibility(
            visible = visible.value,
            enter =  expandHorizontally(
                animationSpec = tween(
                    1500,
                    easing = LinearOutSlowInEasing
                ),
                expandFrom = Alignment.End,
                clip = false,
            )
        ) {
            Image(
                modifier = Modifier
                    .graphicsLayer {
                        rotationY = 180f
                    },
                painter = painterResource(id = R.drawable.train),
                contentDescription = "train"
            )
        }
    }
}

@Preview
@Composable
fun previewSplash(){
    SplashScreen {

    }
}