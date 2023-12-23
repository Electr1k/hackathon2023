package com.xyecos.hackathon.presentation.way

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.xyecos.hackathon.ui.theme.mainBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WayScreen(
    api: ServerApi = ApiModule.provideApi(),
    id: Int,
    popBack: () -> Unit
){
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

    var pickWagon by remember{
        mutableStateOf<Wagon?>(null)
    }

    var way: Resource<Way> by remember{
        mutableStateOf(Resource.Loading())
    }



    LaunchedEffect(true){
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
        if (sheetState.isVisible) {
            ModalBottomSheet(
                containerColor = Color(0XFFFCB53B),
                contentColor = Color.White,
                sheetState = sheetState,
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
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
                    items((way as Resource.Success<Way>).data.locomotives){
                        if (it.direction == Direction.LEFT){
                            LocomotiveBox()
                        }
                    }
                    items((way as Resource.Success<Way>).data.wagonsIds){
                        var wagon: Resource<Wagon> by remember{
                            mutableStateOf(Resource.Loading())
                        }
                        LaunchedEffect(key1 = true){
                            try{
                                wagon = Resource.Success(api.getWagon(it))
                            }
                            catch (e: Exception){
                                wagon = Resource.Error("")
                            }
                        }
                        if (wagon is Resource.Success){
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
                                    }
                                    else{
                                        if ((wagon as Resource.Success<Wagon>).data.id in selectedList){
                                            selectedList.remove((wagon as Resource.Success<Wagon>).data.id)
                                            checkedState.value = false
                                            if (selectedList.size == 0) isSelectionMode = false
                                        }
                                        else{
                                            selectedList.add((wagon as Resource.Success<Wagon>).data.id)
                                            checkedState.value = true
                                        }
                                    }
                                    println(selectedList)
                                },
                                onLongClick = {
                                    println("Лонг клик")
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
                    items((way as Resource.Success<Way>).data.locomotives){
                        if (it.direction == Direction.RIGHT){
                            LocomotiveBox()
                        }
                    }
                    if ((way as Resource.Success<Way>).data.wagonsCount + (way as Resource.Success<Way>).data.locomotives.size == 0){
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
