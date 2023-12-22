package com.xyecos.hackathon.presentation.stations

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateRegistry
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xyecos.hackathon.data.Resource
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.Station
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.common.TopAppBar
import com.xyecos.hackathon.presentation.stations.common.StationCard
import okhttp3.internal.http.RequestLine.get
import javax.inject.Inject

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StationsScreen(
    api: ServerApi = ApiModule.provideApi(),
    navigateToStationById: (id: Int, title: String) -> Unit,
    popBack: () -> Unit
){
    var stations: Resource<List<Station>> by remember{
        mutableStateOf(Resource.Loading())
    }

    LaunchedEffect(true){
        try {
            stations = Resource.Success(api.getStations())
        } catch (e: Exception) {
            stations = Resource.Error(e.message ?: "loading stations error")
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = "Станции")
        }

    ) {
        padding->
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

            when (stations){
                is Resource.Success -> {
                    items((stations as Resource.Success<List<Station>>).data) { station ->
                        StationCard(
                            title = station.title,
                            onClick = { navigateToStationById(station.id, station.title) }
                        )
                    }
                }
                is Resource.Loading -> {
                    println("Загрузка")
                    items(3) {
                        StationCard(
                            title = null,
                            onClick = {}
                        )
                    }
                }

                else -> {}
            }
        }
    }
}