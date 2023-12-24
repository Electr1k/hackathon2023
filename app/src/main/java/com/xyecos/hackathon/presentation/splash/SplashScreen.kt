package com.xyecos.hackathon.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.xyecos.hackathon.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    openApp: () -> Unit
) {
    val visible = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        visible.value = true
        delay(700)
        openApp()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 40.dp)
    ) {
        val (logo, col, text) = createRefs()

        Image(
            modifier = Modifier
                .constrainAs(logo) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom, 64.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 100.dp),
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "logo"
        )

        Text(
            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(logo.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ,
            text = "CyberLogic",
            color = Color.Black,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 50.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
        )


        Column(
            modifier = Modifier
                .constrainAs(col) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .background(Color.White)
                .padding(vertical = 40.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedVisibility(
                visible = visible.value,
                enter = expandHorizontally(
                    animationSpec = tween(
                        2500,
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
}

@Preview
@Composable
fun previewSplash() {
    SplashScreen {

    }
}