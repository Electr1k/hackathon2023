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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.xyecos.hackathon.data.Resource
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.Station
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.common.ScreenHeader
import com.xyecos.hackathon.presentation.common.TopBar
import com.xyecos.hackathon.presentation.stations.common.CustomBox

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun StationsScreen(
    api: ServerApi = ApiModule.provideApi(),
    navigateToStationById: (id: Int, title: String) -> Unit,
    popBack: () -> Unit
) {
    var stations: Resource<List<Station>> by remember {
        mutableStateOf(Resource.Loading())
    }

    var stationsHidden by remember {
        mutableStateOf(true)
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        if (stations is Resource.Loading) {
            try {
                stations = Resource.Success(api.getStations())
            } catch (e: Exception) {
                stations = Resource.Error(e.message ?: "loading stations error")
            }
        }
    }

    var density = LocalDensity.current

    Column {
        TopBar()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ScreenHeader(title = "Список станций", {
                    stationsHidden = !stationsHidden
                })
            }

            items((stations as? Resource.Success<List<Station>>)?.data ?: emptyList()) { station ->
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
                        onClick = { navigateToStationById(station.id, station.title) }
                    )
                }
            }

            item {
                AnimatedVisibility(
                    modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
                    visible = stations is Resource.Success<List<Station>>,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable { /*todo*/ },
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
        }
    }
}