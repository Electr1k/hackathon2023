package com.xyecos.hackathon.presentation.detailed_station

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.data.Resource
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.Park
import com.xyecos.hackathon.data.dto.StationById
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.common.TopAppBar
import com.xyecos.hackathon.presentation.stations.common.CustomBox

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailedStationScreen(
    api: ServerApi = ApiModule.provideApi(),
    id: Int,
    title: String,
    navigationToPark: (id: Int) -> Unit,
    popBack: () -> Unit
){
    var station: Resource<StationById> by remember{
        mutableStateOf(Resource.Loading())
    }
    LaunchedEffect(true){
        if (station is Resource.Loading) {
            try {
                station = Resource.Success(api.getStation(id))
            } catch (e: Exception) {
                station = Resource.Error(e.message ?: "loading stations error")
            }
        }
    }
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
                .padding(
                    top = 30.dp + padding.calculateTopPadding(),
                    start = 30.dp,
                    end = 30.dp,
                    bottom = 30.dp
                ),
            contentPadding = PaddingValues(top = 14.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Парки",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.W500)
                )
            }

            when (station){
                is Resource.Success -> {
                    items((station as Resource.Success<StationById>).data.parksIds) {
                        var park: Resource<Park> by remember{
                            mutableStateOf(Resource.Loading())
                        }
                        if (park is Resource.Loading) {
                            LaunchedEffect(true){
                                try {
                                    park = Resource.Success(api.getPark(it))
                                } catch (e: Exception) {
                                    println(e)
                                }
                            }

                        }
                        if (park is Resource.Success) {
                            CustomBox(
                                text = (park as Resource.Success<Park>).data.name,
                                onClick = { navigationToPark(it) }
                            )
                        }
                        else{
                            CustomBox(
                                text = null,
                                onClick = {}
                            )
                        }
                    }
                }
                is Resource.Loading -> {
                    println("Загрузка")
                    items(3) {
                        CustomBox(
                            text = null,
                            onClick = {}
                        )
                    }
                }
                else -> {}
            }
        }
    }
}