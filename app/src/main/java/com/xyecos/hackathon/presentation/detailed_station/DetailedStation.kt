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
import com.xyecos.hackathon.data.Repo
import com.xyecos.hackathon.data.Resource
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.Park
import com.xyecos.hackathon.data.dto.StationById
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.Loading
import com.xyecos.hackathon.presentation.common.ScreenHeader
import com.xyecos.hackathon.presentation.common.TopBar
import com.xyecos.hackathon.presentation.stations.common.CustomBox

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DetailedStationScreen(
    api: ServerApi = ApiModule.provideApi(),
    id: Int,
    navigationToPark: (id: Int) -> Unit,
) {
    var isLoading by remember {
        mutableStateOf(true)
    }

    var isWarning by remember {
        mutableStateOf(false)
    }

    var parks by remember {
        mutableStateOf(listOf<Park>())
    }

    LaunchedEffect(true) {
        val parksIds = Repo.getFullStations().find { it.id == id }?.parksIds

        parks = Repo.getParks().filter { parksIds?.contains(it.id) ?: false }

        isLoading = false
    }

    Column {
        TopBar(
            "Станция"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                ScreenHeader(
                    title = Repo.getStations().find { it.id == id }?.title ?: " - ",
                    onClick = {},
                    isWarning = isWarning,
                    isLoading = isLoading,
                )
            }

            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        ),
                    text = "Парки",
                    textAlign = TextAlign.Start,
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.W500)
                )
            }

            if (!isLoading) {
                items(parks) { data ->
                    val warnings = Repo.getWagons().filter { it.parkId == data.id }.count {
                        it.isDirty || it.isSick
                    }

                    if (isWarning != (warnings != 0)) {
                        isWarning = warnings != 0
                    }

                    CustomBox(
                        modifier = Modifier.padding(
                            start = 16.dp, end = 16.dp, bottom = 16.dp
                        ),
                        text = data.name,
                        onClick = { navigationToPark(data.id) },
                        firstStr = "Количество путей: ${data.waysCount}",
                        secondStr = "Количество вагонов: ${
                            Repo.getWagons().count { it.parkId == data.id }
                        } / ${
                            Repo.getWays().filter { it.parkId == data.id }
                                .sumOf { it.maxCarriagesCount }
                        }",
                        thirdStr = "Вагонов, требующих внимания: $warnings",
                        isWarning = warnings != 0
                    )
                }
            } else {
                item {
                    Column {
                        Loading()
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Загрузка...",
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.W500)
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun DetailsStationsPreview() {
    DetailedStationScreen(
        id = 1,
        navigationToPark = {},
    )
}