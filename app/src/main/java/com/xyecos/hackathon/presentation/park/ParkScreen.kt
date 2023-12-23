package com.xyecos.hackathon.presentation.park

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.Park
import com.xyecos.hackathon.data.dto.Way
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.common.ScreenHeader
import com.xyecos.hackathon.presentation.common.TopBar
import com.xyecos.hackathon.presentation.stations.common.CustomBox

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ParkScreen(
    id: Int,
    navigationToWay: (id: Int) -> Unit,
) {
    var isLoading by remember {
        mutableStateOf(true)
    }

    var isWarning by remember {
        mutableStateOf(false)
    }

    var ways by remember {
        mutableStateOf(listOf<Way>())
    }

    var park: Park? by remember {
        mutableStateOf(null)
    }

    LaunchedEffect(true) {
        ways = Repo.getWays().filter { it.parkId == id }
        park = Repo.getParks().find { it.id == id }
        isLoading = false
    }

    Column {
        TopBar(
            "Парк",
        )

        ScreenHeader(
            title = "Парк ${park?.name ?: ""}",
            onClick = { },
            isWarning = isWarning,
            isLoading = isLoading,
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isLoading) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                top = 16.dp,
                                bottom = 16.dp
                            ),
                        text = "Пути",
                        textAlign = TextAlign.Start,
                        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.W500)
                    )
                }
                items(ways) { way ->
                    val warnings = Repo.getWagons().count { it.isDirty || it.isSick }

                    if (isWarning != (warnings != 0)) {
                        isWarning = warnings != 0
                    }

                    CustomBox(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp,
                        ),
                        text = way.name,
                        onClick = { navigationToWay(way.id) },
                        firstStr = "${way.locomotives.size} локомотивов на пути",
                        secondStr = "${way.wagonsIds.size} / ${way.maxCarriagesCount} вагонов на пути",
                        thirdStr = "$warnings вагонов требуют внимания",
                        isWarning = warnings != 0
                    )
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
        }
    }
}

@Preview
@Composable
fun ParkScreenPreview() {
    ParkScreen(
        id = 1,
        navigationToWay = {},
    )
}