package com.xyecos.hackathon.presentation.stations

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun StationsScreen(
    navigateToStationById: (id: Int, title: String) -> Unit,
    navigateToMap: () -> Unit,
    navigateToForms: () -> Unit,
    popBack: () -> Unit
) {
    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(true) {
        Repo.ready.filter { it }.collectLatest {
            isLoading = false
        }
    }

    var stationsHidden by remember {
        mutableStateOf(true)
    }

    val density = LocalDensity.current

    Column {
        TopBar()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ScreenHeader(
                    title = "Список станций",
                    onClick = {
                        stationsHidden = !stationsHidden
                    },
                    isLoading = isLoading
                )
            }

            if (!isLoading) {
                items(
                    Repo.getStations()
                ) { station ->
                    val wagons = Repo.getWagons().filter { it.stationId == station.id }
                    val ways = Repo.getWays().filter { it.stationId == station.id }
                    val warns = wagons.count { it.isDirty || it.isSick }

                    AnimatedVisibility(
                        modifier = Modifier.wrapContentSize(),
                        visible = !stationsHidden,
                        enter = slideInVertically {
                            // Slide in from 40 dp from the top.
                            with(density) { -40.dp.roundToPx() }
                        } + expandVertically(
                            // Expand from the top.
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            // Fade in with the initial alpha of 0.3f.
                            initialAlpha = 0.3f
                        ),
                        exit = slideOutVertically() + shrinkVertically() + fadeOut()
                    ) {
                        CustomBox(
                            modifier = if (!stationsHidden) {
                                Modifier
                                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                            } else {
                                Modifier
                            },
                            text = station.title,
                            onClick = { navigateToStationById(station.id, station.title) },
                            firstStr = "${
                                Repo.getFullStations().find { it.id == station.id }?.parksCount ?: 0
                            } парков в системе",
                            secondStr = "${
                                Repo.getWays().filter { it.stationId == station.id }
                                    .sumOf { it.locomotives.size }
                            } локомотивов в системе",
                            thirdStr = "${wagons.size} вагонов в системе",
                            fourthStr = "${warns} вагонов требуют внимания",
                            fifthStr = "${ways.sumOf { it.maxCarriagesCount }} свободных мест",
                            isWarning = warns != 0
                        )
                    }
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 16.dp, bottom = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Loading()
                        Text(
                            text = "Загрузка...",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W600,
                                textAlign = TextAlign.Center
                            ),
                        )
                    }
                }
            }

            item {
                AnimatedVisibility(
                    modifier = Modifier.padding(top = 16.dp),
                    visible = !isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable { navigateToMap() },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFFFFF),
                        ),
                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Row {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 16.dp,
                                    )
                                    .defaultMinSize(minHeight = 48.dp),
                                color = Color.Black,
                                text = "Открыть карту",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.W600,
                                    textAlign = TextAlign.Center
                                ),
                            )
                        }
                    }
                }
            }

            item {
                AnimatedVisibility(
                    modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
                    visible = !isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable { navigateToForms() },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFFFFF),
                        ),
                        elevation = CardDefaults.elevatedCardElevation(4.dp),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Row {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 16.dp,
                                    )
                                    .defaultMinSize(minHeight = 48.dp),
                                color = Color.Black,
                                text = "Список операций",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.W600,
                                    textAlign = TextAlign.Center
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}