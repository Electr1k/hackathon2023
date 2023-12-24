package com.xyecos.hackathon.presentation.way

import android.annotation.SuppressLint
import android.service.autofill.DateTransformation
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.xyecos.hackathon.data.Repo
import com.xyecos.hackathon.data.dto.Locomotive
import com.xyecos.hackathon.data.dto.Park
import com.xyecos.hackathon.data.dto.Station
import com.xyecos.hackathon.data.dto.Wagon
import com.xyecos.hackathon.data.dto.Way
import com.xyecos.hackathon.presentation.common.ScreenHeader
import com.xyecos.hackathon.presentation.common.TopBar
import com.xyecos.hackathon.presentation.way.locomotives.LocomotiveBox
import com.xyecos.hackathon.presentation.way.wagons.Direction
import com.xyecos.hackathon.presentation.way.wagons.WagonButton
import com.xyecos.hackathon.ui.theme.Gray
import com.xyecos.hackathon.ui.theme.GrayTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

enum class Sheet{
    WAGON,
    LOCOMOTIVE,
    MOVE,
    CLOSE
}
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
//    val bottomSheetState = rem(
//        initialValue = SheetValue.Hidden,
//        skipHiddenState = false
//    )


    val bottomSheetLocoState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )

    val moveSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false,
    )
    val moveSheetLocoState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetLocoState)

    var wagons by remember {
        mutableStateOf(listOf<Wagon>())
    }

    var way: Way? by remember {    mutableStateOf(null)
    }
    var park: Park? by remember {    mutableStateOf(null)
    }
    var station: Station? by remember {
        mutableStateOf(null)}
    var waysInMyPark: List<Way>? by remember {
        mutableStateOf(null)}

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
        park = Repo.getParks().find { it.id == way!!.parkId }
        station = Repo.getStations().find { it.id == way!!.stationId }
        waysInMyPark = Repo.getWays().filter { it.id != id && it.parkId == park!!.id && it.stationId == station!!.id }
        isLoading = false
    }

    LaunchedEffect(true) {
        infoSheetState.hide()
        moveSheetState.hide()
        moveSheetLocoState.bottomSheetState.hide()
    }

    // Установлен ли режим выбора вагонов
    var isSelectionMode by remember {
        mutableStateOf(false)
    }

    // Выбранные вагоны
    var selectedIdList =
        remember { mutableStateListOf<Int>() } // Хранит id, можно было и и объекты класса, но перед запросом нужно бы было создать новый список именно с id
    var selectedItemList = remember {
        mutableStateListOf<Wagon>()
    }

    // Вагон, информация о котором будет в нижнем листе
    var pickWagon by remember {
        mutableStateOf<Wagon?>(null)
    }

    var pickLoco by remember {
        mutableStateOf<Locomotive?>(null)
    }


    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetPeekHeight = 0.dp,
        sheetContent = {
            val data = remember{ mutableStateOf<Date?>(null) }
            var expanded by remember { mutableStateOf(false) }
            var name by remember { mutableStateOf<String?>(null) }
            var comment by remember { mutableStateOf<String>("") }

            if (park != null && way != null  && station != null && waysInMyPark != null) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp),
                        text = "Операция №",
                        fontSize = 16.sp,
                        fontWeight = W400
                    )
                    Divider(thickness = 1.dp, color = Color.Gray)
                    Text(
                        modifier = Modifier.padding(top = 8.dp,start = 8.dp),
                        text = "Перемещение вагон" + (if (selectedIdList.size > 1) "ов " else "а " ) + listConvertToString(
                            selectedItemList
                        ),
                        fontSize = 18.sp,
                        fontWeight = W500
                    )
                    Text(
                        modifier = Modifier.padding(top = 3.dp,start = 8.dp),
                        text = "Откуда:",
                        fontSize = 16.sp,
                        fontWeight = W400
                    )

                    Text(
                        modifier = Modifier.padding(top = 5.dp,start = 8.dp),
                        text = station!!.title + ", " + park!!.name + ", " + way!!.name,
                        fontSize = 18.sp,
                        fontWeight = W400
                    )

                    Text(
                        modifier = Modifier.padding(top = 5.dp,start = 8.dp),
                        text = "Куда:",
                        fontSize = 16.sp,
                        fontWeight = W400
                    )

                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = station!!.title + ", " + park!!.name + ", ",
                        fontSize = 18.sp,
                        fontWeight = W400
                    )

                    Box(modifier = Modifier
                        .height(40.dp)
                        .wrapContentSize()
                        .padding(start = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(GrayTopBar)
                        .clickable { expanded = true }, contentAlignment = Alignment.Center
                    ) {
                        Text(text = name?: "Выберите путь", fontSize=18.sp, modifier = Modifier
                            .padding(10.dp))
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            waysInMyPark!!.forEach {
                                Text(it.name, fontSize=18.sp, modifier = Modifier
                                    .padding(10.dp)
                                    .clickable(onClick = { name = it.name }))
                                Divider()
                            }
                        }
                    }

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Black
                        ),
                        placeholder = {
                            Text(text = "Комментарий...")
                        },
                        value = comment,
                        onValueChange = { comment = it }
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    Divider()
                        
                    Spacer(modifier = Modifier.height(5.dp))
                    Row {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .padding(bottom = 2.dp),
                                text = "Тип операции:",
                                fontSize = 16.sp,
                                fontWeight = W400
                            )
                            var extype by remember{ mutableStateOf(false) }

                            var type by remember{ mutableStateOf<String?>(null) }
                            val listType = listOf(
                                "Поездная работа",
                                "Маневровая работа",
                                "Прочие работы",
                                "Хозяйственная работа",
                                "Экипировка",
                                "Обслуживание локомотива",
                                "Технологические перерывы",
                                "Ремонтные работы",
                                "Регламентированные перерывы",
                                "Нетехнологические простои",
                                "Грузовые",
                                "Простой"
                            )
                            Box(modifier = Modifier
                                .height(40.dp)
                                .wrapContentSize()
                                .padding(start = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GrayTopBar)
                                .clickable { extype = true }, contentAlignment = Alignment.Center
                            ) {
                                Text(text = type?: "Выберите тип", fontSize=18.sp, modifier = Modifier
                                    .padding(10.dp))
                                DropdownMenu(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .heightIn(0.dp, 150.dp),
                                    expanded = extype,
                                    onDismissRequest = { extype = false }
                                ) {
                                    listType.forEach {
                                        Text(it, fontSize=18.sp, modifier = Modifier
                                            .padding(10.dp)
                                            .clickable(onClick = { type = it }))
                                        Divider()
                                    }
                                }
                            }
                        }
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .padding(bottom = 2.dp),
                                text = "Операция:",
                                fontSize = 16.sp,
                                fontWeight = W400
                            )
                            var exOp by remember{ mutableStateOf(false) }

                            var op by remember{ mutableStateOf<String?>(null) }
                            val listOp = listOf(
                                "Ведение поезда по перегону", "Движение локомотива резервом по перегону", "Вывод поезда с перегона вспомогательным локомотивом", "Ведение поезда по перегону двумя локомотивами", "Ведение поезда по перегону кратной тягой (груженые вагоны)", "Перестановка вагонов", "Перестановка вагонов без отцепки локомотива", "Подача вагонов", "Уборка вагонов", "Сдвоенная уборка вагонов", "Работа локомотива при погрузке (выгрузке) вагонов", "Осаживание вагонов", "Взвешивание вагонов", "Ст. Междуреченск - перестановка из парка в парк", "Обгон локомотива на станции", "Заезд локомотива с пути на путь", "Подталкивание поезда по станции", "Полное опробование тормозов в поезде одним составителем", "Полное опробование тормозов в поезде двумя составителями", "Сокращённое опробование тормозов в поезде", "Сокращённое опробование по группе подставляемых вагонов", "Установка башмаков", "Отцепка вагонов от состава", "Ст. Междуреченск - полное опробование тормозов в поезде", "Работа локомотива со снегочистом по перегону", "Работа локомотива с ж/д краном на станции", "Ведение хозяйственного поезда по перегону", "Работа локомотива с хозяйственным поездом", "Работа локомотива со снегоочистительной техникой", "Экипировка локомотива дизельным топливом", "Приемка локомотива", "Сдача локомотива", "Техническое обслуживание локомотива ТО-1", "Ожидание свободного пути", "Ожидание подталкивающего локомотива", "ст. Междуреченск - ожидание готовности вагонов", "ст. Междуреченск - ожидание опробования тормозов в поезде", "ст. Междуреченск - ожидание приема поезда", "Ожидание выполнения грузовых операций", "Ожидание закрепления вагонов на путях контрагентов и ст. Междуреченск", "Ремонт локомотива на линии", "Смена колодок", "Смена локомотива", "Замена неисправного локомотива (депо)", "ТО-2", "Устранение неподхода автосцепок", "Ремонт по сцб", "Ремонт подвижного состава", "Ожидание у светофора", "Ожидание дизель-поезда", "Ожидание Междуреченска", "Ожидание перевозочных документов (ст.Междуреченск)", "Ожидание порожней выгрузки", "Ожидание порожняка", "Разрыв состава на ст.Междуреченск", "Простой на ст. Междуреченск", "Колонка на местном управлении(ст.Междуреченск)", "Не переводится стрелка", "Сработали тормоза в поезде ст.Междуреченск", "Ожидание вертушки(ст.М-реченск)", "Ожидание отправления со ст.Междуреченск", "Ожидание отцепки вагонов", "По неисправности пути", "В ожидании работы", "По неисправности локомотива", "Ожидание членов лок-сост.бригады", "Поломка служебного а/транспорта", "Ожидание отцепа", "Ожидание ремонта", "Техническая неисправность", "Сход вагонов", "Ожидание перевозочных документов", "Горячий резерв", "Выгрузка", "Погрузка", "Зачистка вагонов после выгрузки", "Очистка вагонов от снега", "Очистка вагонов от ранее перевозимого груза", "Обработка вагонов перед погрузкой", "Отогрев в тепляке", "Ожидание операции"
                            )
                            Box(modifier = Modifier
                                .height(40.dp)
                                .wrapContentWidth()
                                .height(150.dp)
                                .padding(start = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GrayTopBar)
                                .clickable { exOp = true }, contentAlignment = Alignment.Center
                            ) {
                                Text(text = op?: "Выберите операция", fontSize=16.sp, modifier = Modifier
                                    .padding(10.dp))
                                DropdownMenu(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .heightIn(0.dp, 150.dp),
                                    expanded = exOp,
                                    onDismissRequest = { exOp = false }
                                ) {
                                    listOp!!.forEach {
                                        Text(it, fontSize=18.sp, modifier = Modifier
                                            .padding(10.dp)
                                            .clickable(onClick = { op = it }))
                                        Divider()
                                    }
                                }
                            }
                        }
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .padding(bottom = 2.dp),
                                text = "Причина:",
                                fontSize = 16.sp,
                                fontWeight = W400
                            )
                            var exP by remember{ mutableStateOf(false) }

                            var prich by remember{ mutableStateOf<String?>(null) }
                            val listPrich = listOf(
                                "Формирование подачи под выгрузку",
                                "Подбор по характеристикам вагонов",
                                "Формирование маршрута",
                                "Выработка больных вагонов",
                                "Выработка грязных вагонов",
                                "Выравнивание шапок",
                                "Подбор по собственникам",
                                "Формирование порожняка на отправление",
                                "Подбор вагонов по качеству загруженного угля",
                                "Переформирование маршрута",
                                "Подбор инновационных вагонов"
                            )
                            Box(modifier = Modifier
                                .height(40.dp)
                                .wrapContentWidth()
                                .height(150.dp)
                                .padding(horizontal = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GrayTopBar)
                                .clickable { exP = true }, contentAlignment = Alignment.Center
                            ) {
                                Text(text = prich?: "Причина", fontSize=16.sp, modifier = Modifier
                                    .padding(10.dp))
                                DropdownMenu(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .heightIn(0.dp, 150.dp),
                                    expanded = exP,
                                    onDismissRequest = { exP = false }
                                ) {
                                    listPrich!!.forEach {
                                        Text(it, fontSize=18.sp, modifier = Modifier
                                            .padding(10.dp)
                                            .clickable(onClick = { prich = it }))
                                        Divider()
                                    }
                                }
                            }
                        }

                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 3.dp,end = 8.dp), contentAlignment = Alignment.CenterEnd){
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue), onClick = {
                                scope.launch { moveSheetState.hide() }
                                isSelectionMode = false
                                selectedIdList = mutableStateListOf()
                                selectedItemList = mutableStateListOf()
                            }) {
                            Text(text = "Сохранить", color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(45.dp))

                }
            }
        },
        scaffoldState = rememberBottomSheetScaffoldState(moveSheetState) ){

    if (infoSheetState.isVisible) {
        modalBottomSheet(
            scope = scope,
            sheetState = infoSheetState,
            pickWagon = pickWagon
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
                                        moveSheetState.hide()
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
                            moveSheetState.expand()
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
                                    moveSheetState.hide()
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        textAlign = TextAlign.Center,
                        text = "Путь свободен",
                        fontSize = 24.sp,
                        fontWeight = W600,
                        color = Color(0xFFFF9800)
                    )
                }
            }
            item{
                Spacer(modifier = Modifier.height(if (moveSheetState.currentValue != SheetValue.Expanded)45.dp else 500.dp))
            }
        }

        }
    }
}

fun listConvertToString(list: MutableList<Wagon>): String{    var str = "№"
    list.forEach{        str += "${it.inventoryNumber}" + if (it != list.last()) ", " else ""
    }
    return str}


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
                            .padding(top = 16.dp, bottom = 32.dp)
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
                        .padding(top = 16.dp, bottom = 32.dp)
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



class DateTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return dateFilter(text)
    }
}

fun dateFilter(text: AnnotatedString): TransformedText {

    val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
    var out = ""
    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i % 2 == 1 && i < 4) out += "/"
    }

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 1) return offset
            if (offset <= 3) return offset +1
            if (offset <= 8) return offset +2
            return 10
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <=2) return offset
            if (offset <=5) return offset -1
            if (offset <=10) return offset -2
            return 8
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}