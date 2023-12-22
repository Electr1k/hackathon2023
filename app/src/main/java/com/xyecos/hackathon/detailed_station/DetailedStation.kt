package com.xyecos.hackathon.detailed_station

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.xyecos.hackathon.common.TopAppBar
import com.xyecos.hackathon.stations.common.StationCard

@Composable
fun DetailedStationScreen(
    id: Int,
    title: String,
    popBack: () -> Unit
){
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = title)
        }

    ) { padding->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(
                    top = padding.calculateTopPadding(),
                ),
            contentPadding = PaddingValues(top = 14.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                //Box(modifier = Modifier.fillMaxSize().background(Color.Black)){}
            this.apply {
//                items(stations) { station ->
//                    StationCard(
//                        title = station,
//                        onClick = {}
//                    )
//                }
            }
//        when (event) {
//            is Resource.Success -> {
//                LoadedEvent(
//                    eventWrapper = event.data,
//                    popBackToEvents = popBackToEvents,
//                    exitApp = exitApp
//                )
//            }
//            is Resource.Error -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .paint(painterResource(id = R.drawable.background_trash), contentScale = ContentScale.FillWidth, alignment = Alignment.TopCenter),
//                    contentAlignment = Alignment.Center
//                ) {
//                    PoorConnection {
//                        coroutineScope.launch {
//                            detailedEventViewModel.updateEventData()
//                        }
//                    }
//                }
//            }
//            is Resource.Loading -> {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    LoadingIndicator(
//                        color = DarkBlue,
//                    )
//                }
//            }
//        }
        }
    }
}