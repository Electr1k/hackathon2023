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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Scaffold
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
import com.xyecos.hackathon.data.Resource
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.Wagon
import com.xyecos.hackathon.data.dto.Way
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.stations.common.CustomBox
import com.xyecos.hackathon.presentation.way.locomotives.LocomotiveBox
import com.xyecos.hackathon.presentation.way.wagons.Direction
import com.xyecos.hackathon.presentation.way.wagons.WagonButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter",
    "MutableCollectionMutableState", "UnrememberedMutableState"
)
@Composable
fun WayScreen(
    api: ServerApi = ApiModule.provideApi(),
    id: Int,
    popBack: () -> Unit
){
    val scope = rememberCoroutineScope()

    // Нижний лист для информации о вагоне
    val infoSheetState = rememberModalBottomSheetState()

    // Нижний лист для перегона вагонов
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val moveSheetState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    scope.launch {
        infoSheetState.hide()
        moveSheetState.bottomSheetState.hide()
    }

    // Установлен ли режим выбора вагонов
    var isSelectionMode by remember {
        mutableStateOf(false)
    }

    // Выбранные вагоны
    val selectedIdList = remember { mutableStateListOf<Int>()} // Хранит id, можно было и и объекты класса, но перед запросом нужно бы было создать новый список именно с id
    val selectedItemList = remember {
        mutableStateListOf<Wagon>()
    }


    var density = LocalDensity.current

    // Вагон, информация о котором будет в нижнем листе
    var pickWagon by remember{
        mutableStateOf<Wagon?>(null)
    }

    var way: Resource<Way> by remember{
        mutableStateOf(Resource.Loading())
    }

    val visible = remember {
        mutableStateOf(false)
    }
    // Запрос к пути
    LaunchedEffect(true){
        visible.value = true
        if (way is Resource.Loading) {
            way = try {
                Resource.Success(api.getWay(id))
            } catch (e: Exception) {
                Resource.Error(e.message ?: "loading stations error")
            }
        }

    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { com.xyecos.hackathon.presentation.common.TopAppBar((way as? Resource.Success)?.data?.name ?: "")}) {
        if (infoSheetState.isVisible) {
            InfoModalBottomSheet(scope = scope, infoSheetState = infoSheetState, pickWagon = pickWagon)
        }

        if (moveSheetState.bottomSheetState.isVisible) {
            MoveModalBottomSheet(scope = scope, moveSheetState = moveSheetState, selectedList = selectedIdList)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp + it.calculateTopPadding()),
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            when (way){
                is Resource.Success -> {
                    val wayData = (way as Resource.Success<Way>).data
                    // Рисуем локомотивы, который находятся слева
                    items(wayData.locomotives){
                        if (it.direction == Direction.LEFT){
                            LocomotiveBox()
                        }
                    }
                    // Рисуем вагоны
                    
                    items(wayData.wagonsIds){
                            var wagon: Resource<Wagon> by remember {
                                mutableStateOf(Resource.Loading())
                            }
                        LaunchedEffect(key1 = true) {
                            wagon = try {
                                Resource.Success(api.getWagon(it))
                            } catch (e: Exception) {
                                Resource.Error("")
                            }
                        }
                        if (wagon is Resource.Success) {
                            val checkedState = remember { mutableStateOf(false) }
                            val wagonData = (wagon as Resource.Success<Wagon>).data
                            AnimatedVisibility(
                                modifier = Modifier.wrapContentSize(),
                                visible = visible.value,
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
                                    wagon = wagonData,
                                    onClick = {
                                        if (!isSelectionMode) {
                                            pickWagon = wagonData
                                            scope.launch {
                                                infoSheetState.show()
                                            }
                                        } else {
                                            if (wagonData.id in selectedIdList) {
                                                selectedIdList.remove(wagonData.id)
                                                selectedItemList.remove(wagonData)
                                                checkedState.value = false
                                                if (selectedIdList.size == 0) {
                                                    isSelectionMode = false
                                                    scope.launch {
                                                        moveSheetState.bottomSheetState.hide()
                                                    }
                                                }
                                            } else {
                                                selectedIdList.add(wagonData.id)
                                                selectedItemList.add(wagonData)
                                                checkedState.value = true
                                            }
                                        }
                                    },
                                    onLongClick = {
                                        isSelectionMode = true
                                        selectedIdList.add(wagonData.id)
                                        selectedItemList.add(wagonData)
                                        checkedState.value = true
                                        scope.launch {
                                            moveSheetState.bottomSheetState.expand()
                                        }
                                    },
                                    isChecked = checkedState.value,
                                    isSelectionMode = isSelectionMode,
                                    onChangeCheck = {
                                        if (wagonData.id in selectedIdList) {
                                            selectedIdList.remove(wagonData.id)
                                            checkedState.value = false
                                            if (selectedIdList.size == 0) {
                                                isSelectionMode = false
                                                scope.launch {
                                                    moveSheetState.bottomSheetState.hide()
                                                }
                                            }
                                        } else {
                                            selectedIdList.add(wagonData.id)
                                            selectedItemList.add(wagonData)
                                            checkedState.value = true
                                        }
                                    }
                                )
                        }
                        }
                    }
                    // Рисуем локомотивы, который находятся слева
                    items(wayData.locomotives){
                        if (it.direction == Direction.RIGHT){
                            LocomotiveBox()
                        }
                    }
                    if (wayData.wagonsCount + wayData.locomotives.size == 0){
                        item {
                            Text(modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, text = "Путь свободен", fontSize = 24.sp, fontWeight = W600, color = Color(0xFF0B2C62))
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
){
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
                    fontWeight = W500)
                Text(
                    text = "Собственник № ${pickWagon!!.owner}",
                    fontSize = 18.sp,
                    fontWeight = W500)
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
){
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
        }}

    ) {

    }
}