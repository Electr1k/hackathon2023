package com.xyecos.hackathon.presentation.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.data.MockRepo
import com.xyecos.hackathon.presentation.common.ScreenHeader
import com.xyecos.hackathon.presentation.common.TopBar
import com.xyecos.hackathon.presentation.stations.common.CustomBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockFormsContent(
    modifier: Modifier = Modifier,
) {
    val modal1State = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val modal2State = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val scope1 = rememberCoroutineScope()
    val scope2 = rememberCoroutineScope()

    if (modal1State.isVisible) {
        modal1(sheetState = modal1State, scope = scope1)
    }
    if (modal2State.isVisible) {
        modal2(sheetState = modal2State, scope = scope2, ok = {
            MockRepo.mockOperations[1] = MockRepo.mockOperations[1].copy(
                operationStatus = "DONE",
                comment = it
            )
        }, decline = {
            MockRepo.mockOperations[1] = MockRepo.mockOperations[1].copy(
                operationStatus = "DECLINED",
                comment = it
            )
        })
    }

    Column {
        TopBar(
            "Операции"
        )

        ScreenHeader(
            title = "Список операций",
            onClick = { /*TODO*/ },
            firstStr = "Всего - 1",
            secondStr = "Открытых - 2"
        )

        MockFormShort(
            {
                scope1.launch {
                    modal1State.show()
                }
            },
            {
                scope2.launch {
                    modal2State.show()
                }
            }
        )

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun modal1(
    sheetState: SheetState,
    scope: CoroutineScope,
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
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "ID - 23121",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "VN - 23112",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Начало - 2023-12-22 06:37:46",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Окончание - 2023-12-22 07:37:46",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Имя операции - Подача вагонов",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Причина - Формирование подачи под выгрузку",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Норма длит. мин. - 10",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "ID Вагонов - 1",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Станция отпр. - ID 1 - \"Новокузнецк-Северный\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Парк отпр. - ID 10 - \"ТЭЦ\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Путь отпр. - ID 1 - \"10А\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Станция приб. - ID 3 - \"Курегеш\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Парк приб. - ID 5423 - \"1К\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Путь приб. - ID 232 - \"29\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Направление - Слева",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Локомотив - ID 1 - 422412 - Слева",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Статус - ВЫПОЛНЕНО",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )

//                OutlinedButton(
//                    onClick = {/*todo*/ },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top=16.dp, bottom = 32.dp)
//                        .height(50.dp)
//                    ,
//                    border = BorderStroke(2.dp, Color.Black),
//                    shape = RoundedCornerShape(4.dp)
//                ) {
//                    Text(
//                        text = "Выполнить операцию",
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center,
//                        color = Color.Black,
//                    )
//                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun modal2(
    sheetState: SheetState,
    scope: CoroutineScope,
    ok: (String) -> Unit,
    decline: (String) -> Unit
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
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "ID - 3121",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "VN - 3121",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Начало - 2023-12-24 06:37:46",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Окончание - 2023-12-24 07:37:46",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Имя операции - Подача вагонов",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Причина - выравнивание шапок",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Норма длит. мин. - 10",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "ID Вагонов - 1",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Станция отпр. - ID 1 - \"Новокузнецк-Северный\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Парк отпр. - ID 10 - \"ТЭЦ\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Путь отпр. - ID 1 - \"10А\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Станция приб. - ID 3 - \"Курегеш\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Парк приб. - ID 5423 - \"1К\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Путь приб. - ID 232 - \"29\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Направление - Слева",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Локомотив - ID 1 - 422412 - Слева",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Комментарий - ${MockRepo.mockOperations[1].comment}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Text(
                    text = "Статус - ${
                        when (MockRepo.mockOperations[1].operationStatus) {
                            "OPEN" -> {
                                "ОТКРЫТО"
                            }

                            "DONE" -> {
                                "ВЫПОЛНЕНО"
                            }

                            "DECLINED" -> {
                                "ОТМЕНА"
                            }

                            else -> {
                                "ОТМЕНА"
                            }
                        }
                    }",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )

                if (MockRepo.mockOperations[1].operationStatus == "OPEN") {
                    var text: String by remember {
                        mutableStateOf("")
                    }

                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Black
                        ),
                        placeholder = {
                            Text(text = "Комментарий...")
                        },
                        value = text,
                        onValueChange = { text = it }
                    )

                    Row {
                        OutlinedButton(
                            onClick = {
                                ok(text)
                                scope.launch {
                                    sheetState.hide()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 16.dp, bottom = 32.dp, end = 8.dp)
                                .height(50.dp),
                            border = BorderStroke(2.dp, Color.Black),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Подтвердить",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = Color.Black,
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                decline(text)
                                scope.launch {
                                    sheetState.hide()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 16.dp, bottom = 32.dp, start = 8.dp)
                                .height(50.dp),
                            border = BorderStroke(2.dp, Color.Black),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Отклонить",
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
}

@Composable
fun MockFormShort(openModal1: () -> Unit, openModal2: () -> Unit) {
    Column {
        CustomBox(
            modifier = Modifier.padding(
                top = 32.dp,
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            text = "№ 23121",
            onClick = { openModal1() },
            firstStr = "Подача Вагонов - ВЫПОЛНЕНО",
            secondStr = "Формирование подачи под выгрузку",
            thirdStr = "Новокузнецк-Северный - ТЭЦ - 10А",
            fourthStr = "Курегеш - 1К - 29 (Слева)",
            fifthStr = "Локомотив - 422412 ID 1",
            sixthStr = "Начало - 2023-12-22 06:37:46",
            seventhStr = "Окончание - 2023-12-22 07:37:46",
            isWarning = false
        )

        CustomBox(
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 32.dp,
                start = 16.dp,
                end = 16.dp
            ),
            text = "№ 3121",
            onClick = { openModal2() },
            firstStr = "Подача Вагонов - " + when (MockRepo.mockOperations[1].operationStatus) {
                "OPEN" -> {
                    "ОТКРЫТО"
                }

                "DONE" -> {
                    "ВЫПОЛНЕНО"
                }

                "DECLINED" -> {
                    "ОТМЕНА"
                }

                else -> {
                    "ОТМЕНА"
                }
            },
            secondStr = "Выравнивание шапок",
            thirdStr = "Новокузнецк-Северный - ТЭЦ - 10А",
            fourthStr = "Курегеш - 1К - 29 (Слева)",
            fifthStr = "Локомотив - 422412 ID 1",
            sixthStr = "Начало - 2023-12-24 06:37:46",
            seventhStr = if (MockRepo.mockOperations[1].operationStatus == "OPEN") null else "Окончание - 2023-12-24 06:37:46",
            isWarning = false
        )
    }
}

@Preview
@Composable
fun MockFormsContentPreview() {
    MockFormsContent()
}

@Preview
@Composable
fun MockFormShortPreview() {
//    MockFormShort()
}