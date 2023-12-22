package com.xyecos.hackathon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.xyecos.hackathon.navigation.NavigationGraph

@Composable
fun MainScreen(){
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .background(Color.Transparent),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                )
                .fillMaxSize()
        ) {
            NavigationGraph(
                navController = navController
            )
        }
    }
}