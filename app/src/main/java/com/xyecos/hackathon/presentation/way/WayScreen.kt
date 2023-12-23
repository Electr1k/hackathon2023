package com.xyecos.hackathon.presentation.way

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.data.Repo
import com.xyecos.hackathon.data.Resource
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.Wagon
import com.xyecos.hackathon.data.dto.Way
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.way.locomotives.LocomotiveBox
import com.xyecos.hackathon.presentation.way.wagons.Direction
import com.xyecos.hackathon.presentation.way.wagons.WagonButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint(
    "CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter",
    "MutableCollectionMutableState", "UnrememberedMutableState"
)
@Composable
fun WayScreen(
    api: ServerApi = ApiModule.provideApi(),
    id: Int,
) {
    // Нижний лист для информации о вагоне
    val infoSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // Нижний лист для перегона вагонов
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val moveSheetState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    var wagons by remember {
        mutableStateOf(listOf<Wagon>())
    }

    var way: Way? by remember {
        mutableStateOf(null)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var isWarning by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        Repo.getWagons().filter { it.wayId == id }.let {
            wagons = it
        }

        way = Repo.getWays().find { it.id == id }

        isLoading = false
    }

    LaunchedEffect(true) {
        infoSheetState.hide()
        moveSheetState.bottomSheetState.hide()
    }

    // Установлен ли режим выбора вагонов
    var isSelectionMode by remember {
        mutableStateOf(false)
    }

    // Выбранные вагоны
    val selectedIdList =
        remember { mutableStateListOf<Int>() } // Хранит id, можно было и и объекты класса, но перед запросом нужно бы было создать новый список именно с id
    val selectedItemList = remember {
        mutableStateListOf<Wagon>()
    }


    var density = LocalDensity.current

    // Вагон, информация о котором будет в нижнем листе
    var pickWagon by remember {
        mutableStateOf<Wagon?>(null)
    }

    if (infoSheetState.isVisible) {
        InfoModalBottomSheet(
            scope = scope,
            infoSheetState = infoSheetState,
            pickWagon = pickWagon
        )
    }

    if (moveSheetState.bottomSheetState.isVisible) {
        MoveModalBottomSheet(
            scope = scope,
            moveSheetState = moveSheetState,
            selectedList = selectedIdList
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        val wayData = (way as Resource.Success<Way>).data
        // Рисуем локомотивы, который находятся слева
        items(wayData.locomotives) {
            if (it.direction == Direction.LEFT) {
                LocomotiveBox()
            }
        }
        // Рисуем вагоны

        items(wagons) { wagon ->
            val checkedState = remember { mutableStateOf(false) }
            AnimatedVisibility(
                modifier = Modifier.wrapContentSize(),
                visible = !isLoading,
                enter = slideInVertically {
                    // Slide in from 72 dp from the top.
                    with(density) { -72.dp.roundToPx() }
                } + expandVertically(
                    // Expand from the top.
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    // Fade in with the initial alpha of 0.3f.
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                WagonButton(
                    wagon = wagon,
                    onClick = {
                        if (!isSelectionMode) {
                            pickWagon = wagon
                            scope.launch {
                                infoSheetState.show()
                            }
                        } else {
                            if (wagon.id in selectedIdList) {
                                selectedIdList.remove(wagon.id)
                                selectedItemList.remove(wagon)
                                checkedState.value = false
                                if (selectedIdList.size == 0) {
                                    isSelectionMode = false
                                    scope.launch {
                                        moveSheetState.bottomSheetState.hide()
                                    }
                                }
                            } else {
                                selectedIdList.add(wagon.id)
                                selectedItemList.add(wagon)
                                checkedState.value = true
                            }
                        }
                    },
                    onLongClick = {
                        isSelectionMode = true
                        selectedIdList.add(wagon.id)
                        selectedItemList.add(wagon)
                        checkedState.value = true
                        scope.launch {
                            moveSheetState.bottomSheetState.expand()
                        }
                    },
                    isChecked = checkedState.value,
                    isSelectionMode = isSelectionMode,
                    onChangeCheck = {
                        if (wagon.id in selectedIdList) {
                            selectedIdList.remove(wagon.id)
                            checkedState.value = false
                            if (selectedIdList.size == 0) {
                                isSelectionMode = false
                                scope.launch {
                                    moveSheetState.bottomSheetState.hide()
                                }
                            }
                        } else {
                            selectedIdList.add(wagon.id)
                            selectedItemList.add(wagon)
                            checkedState.value = true
                        }
                    }
                )
            }
        }
        // Рисуем локомотивы, который находятся слева
        items(wayData.locomotives) {
            if (it.direction == Direction.RIGHT) {
                LocomotiveBox()
            }
        }
        if (wayData.wagonsCount + wayData.locomotives.size == 0) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Путь свободен",
                    fontSize = 24.sp,
                    fontWeight = W600,
                    color = Color(0xFF0B2C62)
                )
            }
        }
    }
}

/**
 * param [scope] - coroutineScope
 * param [infoSheetState] - state for this bottom sheet
 * pickWagon - selected Wagon
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun InfoModalBottomSheet(
    scope: CoroutineScope,
    infoSheetState: SheetState,
    pickWagon: Wagon?,
) {
    ModalBottomSheet(
        containerColor = Color(0XFFFCB53B),
        contentColor = Color.White,
        sheetState = infoSheetState,
        onDismissRequest = {
            scope.launch {
                infoSheetState.hide()
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
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
                    text = "Собственник № ${pickWagon!!.owner}",
                    fontSize = 18.sp,
                    fontWeight = W500
                )
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun MoveModalBottomSheet(
    scope: CoroutineScope,
    moveSheetState: BottomSheetScaffoldState,
    selectedList: MutableList<Int>,
) {
    BottomSheetScaffold(
        containerColor = Color(0XFFFCB53B),
        contentColor = Color.White,
        scaffoldState = moveSheetState,
        sheetContent = {

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Перемещение вагонов",
                    fontSize = 18.sp,
                    fontWeight = W500,
                    //color = Colors.
                )
            }
        }

    ) {

    }
}