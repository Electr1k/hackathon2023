package com.xyecos.hackathon.presentation.way

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.data.Resource
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.Wagon
import com.xyecos.hackathon.data.dto.Way
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.common.ScreenHeader
import com.xyecos.hackathon.presentation.common.TopBar
import com.xyecos.hackathon.presentation.stations.common.CustomBox
import com.xyecos.hackathon.presentation.way.locomotives.LocomotiveBox
import com.xyecos.hackathon.presentation.way.wagons.Direction
import com.xyecos.hackathon.presentation.way.wagons.WagonButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WayScreen(
    api: ServerApi = ApiModule.provideApi(),
    id: Int,
    popBack: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    scope.launch {
        sheetState.hide()
    }

    var selectedList = remember {
        mutableListOf<Int>()
    }

    var isSelectionMode by remember {
        mutableStateOf(false)
    }

    var pickWagon by remember {
        mutableStateOf<Wagon?>(null)
    }

    var way: Resource<Way> by remember {
        mutableStateOf(Resource.Loading())
    }

    LaunchedEffect(true) {
        if (way is Resource.Loading) {
            way = try {
                Resource.Success(api.getWay(id))
            } catch (e: Exception) {
                Resource.Error(e.message ?: "loading stations error")
            }
        }
    }

    if (sheetState.isVisible) {
        modalBottomSheet(sheetState, scope, pickWagon)
    }

    Column {
        TopBar(extraText = "Путь")

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                ScreenHeader(
                    title = ((("Путь " + (way as? Resource.Success)?.data?.name))), {}
                )
            }

            when (way) {
                is Resource.Success -> {
                    items((way as Resource.Success<Way>).data.locomotives) {
                        if (it.direction == Direction.LEFT) {
                            LocomotiveBox(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                //todo инфо про локомотив
                            )
                        }
                    }
                    items((way as Resource.Success<Way>).data.wagonsIds) {
                        var wagon: Resource<Wagon> by remember {
                            mutableStateOf(Resource.Loading())
                        }
                        LaunchedEffect(key1 = true) {
                            try {
                                wagon = Resource.Success(api.getWagon(it))
                            } catch (e: Exception) {
                                wagon = Resource.Error("")
                            }
                        }
                        if (wagon is Resource.Success) {
                            val checkedState = remember { mutableStateOf(false) }
                            WagonButton(
                                wagon = (wagon as Resource.Success<Wagon>).data,
                                onClick = {

                                    if (!isSelectionMode) {
                                        pickWagon = ((wagon as Resource.Success<Wagon>).data)
                                        showBottomSheet = !showBottomSheet
                                        scope.launch {
                                            sheetState.show()
                                        }
                                    } else {
                                        if ((wagon as Resource.Success<Wagon>).data.id in selectedList) {
                                            selectedList.remove((wagon as Resource.Success<Wagon>).data.id)
                                            checkedState.value = false
                                            if (selectedList.size == 0) isSelectionMode = false
                                        } else {
                                            selectedList.add((wagon as Resource.Success<Wagon>).data.id)
                                            checkedState.value = true
                                        }
                                    }
                                    println(selectedList)
                                },
                                onLongClick = {
                                    isSelectionMode = true
                                    selectedList.add((wagon as Resource.Success<Wagon>).data.id)
                                    checkedState.value = true
                                },
                                isChecked = checkedState.value,
                                isSelectionMode = isSelectionMode,
                                onChangeCheck = {}
                            )
                        }
                    }
                    items((way as Resource.Success<Way>).data.locomotives) {
                        if (it.direction == Direction.RIGHT) {
                            LocomotiveBox()
                            //todo инфо про локомотив
                        }
                    }
                    if ((way as Resource.Success<Way>).data.wagonsCount + (way as Resource.Success<Way>).data.locomotives.size == 0) {
                        item {
                            Text(
                                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                                textAlign = TextAlign.Center,
                                text = "Путь свободен",
                                fontSize = 24.sp,
                                fontWeight = W600,
                                color = Color(0xFF0B2C62)
                            )
                        }
                    }
                }

                is Resource.Loading -> {
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            text = "Загрузка...",
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 22.sp, fontWeight = W500)
                        )
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun modalBottomSheet(
    sheetState: SheetState,
    scope: CoroutineScope,
    pickWagon: Wagon?
) {
    ModalBottomSheet(
        modifier = Modifier,
        containerColor = Color.White,
        sheetState = sheetState,
        dragHandle = {},
        windowInsets = WindowInsets(top = 0.dp),
        shape = RoundedCornerShape(0.dp),
        scrimColor = Color.Transparent,
        tonalElevation = 8.dp,
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }
        },
    ) {
        Column() {
            Box(
                modifier = Modifier
                    .background(color = Color(0xfffcb53b))
                    .fillMaxWidth()
                    .width(2.dp)
                    .padding(bottom = 4.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (pickWagon != null) {
                    Text(
                        text = "Вагон № ${pickWagon!!.inventoryNumber}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Простой по станции ${pickWagon!!.idleDaysLength} дней",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Собственник - ${pickWagon!!.owner}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun WayScreen() {
    WayScreen(id = 1, popBack = {})
}
