package com.xyecos.hackathon.presentation.detailed_station

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.data.Resource
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.Park
import com.xyecos.hackathon.data.dto.StationById
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.common.ScreenHeader
import com.xyecos.hackathon.presentation.common.TopBar
import com.xyecos.hackathon.presentation.stations.common.CustomBox

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailedStationScreen(
    api: ServerApi = ApiModule.provideApi(),
    id: Int,
    title: String,
    navigationToPark: (id: Int) -> Unit,
    popBack: () -> Unit
) {
    var station: Resource<StationById> by remember {
        mutableStateOf(Resource.Loading())
    }
    LaunchedEffect(true) {
        if (station is Resource.Loading) {
            try {
                station = Resource.Success(api.getStation(id))
            } catch (e: Exception) {
                station = Resource.Error(e.message ?: "loading stations error")
            }
        }
    }
    Column {
        TopBar(
            "Станция"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 30.dp,
                    end = 30.dp,
                    bottom = 30.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 16.dp,
                            bottom = 16.dp
                        ),
                    text = "Парки",
                    textAlign = TextAlign.Start,
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.W500)
                )
            }

            when (station) {
                is Resource.Success -> {
                    items((station as Resource.Success<StationById>).data.parksIds) {
                        var park: Resource<Park> by remember {
                            mutableStateOf(Resource.Loading())
                        }
                        if (park is Resource.Loading) {
                            LaunchedEffect(true) {
                                try {
                                    park = Resource.Success(api.getPark(it))
                                } catch (e: Exception) {
                                    println(e)
                                }
                            }

                        }
                        if (park is Resource.Success) {
                            CustomBox(
                                modifier = Modifier.padding(
                                    bottom = 16.dp,
                                ),
                                text = (park as Resource.Success<Park>).data.name,
                                onClick = { navigationToPark(it) }
                            )
                        }
                    }
                }

                is Resource.Loading -> {
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Загрузка...",
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.W500)
                        )
                    }
                }

                else -> {}
            }
        }
    }
}

@Preview
@Composable
fun DetailsStationsPreview() {
    DetailedStationScreen(
        id = 1,
        title = "Станция",
        navigationToPark = {},
        popBack = {}
    )
}