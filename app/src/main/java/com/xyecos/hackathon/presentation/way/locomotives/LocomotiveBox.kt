package com.xyecos.hackathon.presentation.way.locomotives

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xyecos.hackathon.R
import com.xyecos.hackathon.ui.theme.Locomotive

@Composable
fun LocomotiveBox(){
   Box(
       modifier = Modifier
           .size(72.dp)
           .border(2.dp, Locomotive),
       contentAlignment = Alignment.Center
   ){
       Image(painter = painterResource(id = R.drawable.locomotive_icon), contentDescription = null, modifier = Modifier.size(32.dp))
   }
}