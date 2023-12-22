package com.xyecos.hackathon.stations

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xyecos.hackathon.common.TopAppBar
import com.xyecos.hackathon.stations.common.StationCard

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StationsScreen(
    navigateToStationById: (id: Int, title: String) -> Unit,
    popBack: () -> Unit
){

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = "Станции")
        }

    ) {
        padding->
        val stations = listOf("Новокузнецк-Северный", "Томусинская", "Курегеш")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                ),
            contentPadding = PaddingValues(top = 14.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            this.apply {
                items(stations) { station ->
                    StationCard(
                        title = station,
                        onClick = { navigateToStationById(0, station) }
                    )
                }
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