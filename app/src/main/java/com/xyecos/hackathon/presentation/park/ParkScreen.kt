package com.xyecos.hackathon.presentation.park

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
import com.xyecos.hackathon.data.dto.Way
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.common.ScreenHeader
import com.xyecos.hackathon.presentation.common.TopBar
import com.xyecos.hackathon.presentation.stations.common.CustomBox

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ParkScreen(
    api: ServerApi = ApiModule.provideApi(),
    id: Int,
    navigationToWay: (id: Int) -> Unit,
    popBack: () -> Unit
) {
    var park: Resource<Park> by remember {
        mutableStateOf(Resource.Loading())
    }
    LaunchedEffect(true) {
        if (park is Resource.Loading) {
            try {
                park = Resource.Success(api.getPark(id))
            } catch (e: Exception) {
                park = Resource.Error(e.message ?: "loading stations error")
            }
        }
    }

    Column {
        TopBar(
            "Парк",
        )

        ScreenHeader(
            title = "Парк",
            onClick = { },
            isWarning = false
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 30.dp,
                    end = 30.dp,
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
                    text = "Пути",
                    textAlign = TextAlign.Start,
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.W500)
                )
            }
            when (park) {
                is Resource.Success -> {
                    items((park as Resource.Success<Park>).data.waysIds) {
                        var way: Resource<Way> by remember {
                            mutableStateOf(Resource.Loading())
                        }
                        if (way is Resource.Loading) {
                            LaunchedEffect(true) {
                                try {
                                    way = Resource.Success(api.getWay(it))
                                } catch (e: Exception) {
                                    println(e)
                                }
                            }
                        }

                        if (way is Resource.Success) {
                            CustomBox(
                                modifier = Modifier.padding(
                                    bottom = 16.dp,
                                ),
                                text = (way as Resource.Success<Way>).data.name,
                                onClick = { navigationToWay((way as Resource.Success<Way>).data.id) }
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
fun ParkScreenPreview() {
    ParkScreen(
        id = 1,
        navigationToWay = {},
        popBack = {}
    )
}