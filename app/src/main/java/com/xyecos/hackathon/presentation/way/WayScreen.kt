package com.xyecos.hackathon.presentation.way

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import com.xyecos.hackathon.data.dto.Locomotive
import com.xyecos.hackathon.data.dto.Wagon
import com.xyecos.hackathon.data.dto.Way
import com.xyecos.hackathon.presentation.common.ScreenHeader
import com.xyecos.hackathon.presentation.common.TopBar
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

    val bottomSheetLocoState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )

    val moveSheetState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    val moveSheetLocoState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetLocoState)

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
        moveSheetLocoState.bottomSheetState.hide()
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

    // Вагон, информация о котором будет в нижнем листе
    var pickWagon by remember {
        mutableStateOf<Wagon?>(null)
    }

    var pickLoco by remember {
        mutableStateOf<Locomotive?>(null)
    }

    if (infoSheetState.isVisible) {
        modalBottomSheet(
            scope = scope,
            sheetState = infoSheetState,
            pickWagon = pickWagon
        )
    }

    if (moveSheetState.bottomSheetState.isVisible) {
        MoveModalBottomSheet(
            moveSheetState = moveSheetState,
            selectedList = selectedIdList
        )
    }

    if (moveSheetLocoState.bottomSheetState.isVisible) {
        modalBottomSheetForLoco(
            sheetState = moveSheetLocoState.bottomSheetState,
            scope = rememberCoroutineScope(),
            locomotive = pickLoco
        )
    }

    Column {
        TopBar(extraText = "Путь")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                ScreenHeader(
                    title = ((("Путь " + way?.name))), {},
                    isLoading = isLoading,
                    isWarning = isWarning
                )
            }

            // Рисуем локомотивы, который находятся слева
            items(way?.locomotives ?: emptyList()) {
                if (it.direction == Direction.LEFT) {
                    LocomotiveBox(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp
                        ),
                        onClick = {
                            if (!isSelectionMode) {
                                pickLoco = it
                                scope.launch {
                                    moveSheetLocoState.bottomSheetState.expand()
                                }
                            }
                        },
                        locomotive = it,
                    )
                }
            }

            items(wagons) { wagon ->
                val warn = wagon.isDirty || wagon.isSick

                if (isWarning != wagon.isDirty || wagon.isSick) {
                    isWarning = wagon.isDirty || wagon.isSick
                }

                val checkedState = remember { mutableStateOf(false) }

                WagonButton(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp
                    ),
                    isWarning = warn,
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
            // Рисуем локомотивы, который находятся слева
            items(way?.locomotives ?: emptyList()) {
                if (it.direction == Direction.RIGHT) {
                    LocomotiveBox(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 32.dp
                        ),
                        onClick = {},
                        locomotive = it,
                    )
                }
            }
            if ((way?.wagonsCount ?: 0) + (way?.locomotives?.size ?: 0) == 0) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        textAlign = TextAlign.Center,
                        text = "Путь свободен",
                        fontSize = 24.sp,
                        fontWeight = W600,
                        color = Color(0xFFFF9800)
                    )
                }
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
                    .verticalScroll(rememberScrollState())
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                if (pickWagon != null) {
                    Text(
                        text = "Вагон № ${pickWagon.inventoryNumber}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "ID в системе - ${pickWagon.inventoryNumber}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Простой на станции ${pickWagon.idleDaysLength} дней",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Собственник - ${pickWagon.owner}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Тип - ${pickWagon.type}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Состояние - ${pickWagon.operationState}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Дней до обслуживания - ${pickWagon.daysToRepair}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Грузоподъёмность - ${pickWagon.loadCapacity}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Груз - ${pickWagon.cargo}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Осталось км. - ${pickWagon.kilometersLeft}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Наличие люка - ${if (pickWagon.isWithHatch) "Есть" else "Нет"}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Грязный - ${if (pickWagon.isDirty) "Да" else "Нет"}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Сломан - ${if (pickWagon.isSick) "Да" else "Нет"}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Дополнительная информация 1 - ${pickWagon.note1}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        text = "Дополнительная информация 2 - ${pickWagon.note2}",
                        fontSize = 18.sp,
                        fontWeight = W500
                    )

                    OutlinedButton(
                        onClick = {/*todo*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top=16.dp, bottom = 32.dp)
                            .height(50.dp)
                        ,
                        border = BorderStroke(2.dp, Color.Black),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Выполнить операцию",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun modalBottomSheetForLoco(
    sheetState: SheetState,
    scope: CoroutineScope,
    locomotive: Locomotive?
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
                    .verticalScroll(rememberScrollState())
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                OutlinedButton(
                    onClick = {/*todo*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top=16.dp, bottom = 32.dp)
                        .height(50.dp)
                    ,
                    border = BorderStroke(2.dp, Color.Black),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Выполнить операцию",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun MoveModalBottomSheet(
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