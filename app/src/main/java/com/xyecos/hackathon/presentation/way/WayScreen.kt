package com.xyecos.hackathon.presentation.way

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.data.Resource
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.Wagon
import com.xyecos.hackathon.data.dto.Way
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.common.TopAppBar
import com.xyecos.hackathon.presentation.stations.common.CustomBox
import com.xyecos.hackathon.presentation.way.locomotives.LocomotiveBox
import com.xyecos.hackathon.presentation.way.wagons.Direction
import com.xyecos.hackathon.presentation.way.wagons.WagonButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WayScreen(
    api: ServerApi = ApiModule.provideApi(),
    id: Int,
    popBack: () -> Unit
){
    val sheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    val scaffoldState = androidx.compose.material.rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    var pickWagon by remember{
        mutableStateOf<Wagon?>(null)
    }
    val scope = rememberCoroutineScope()

    var way: Resource<Way> by remember{
        mutableStateOf(Resource.Loading())
    }
    LaunchedEffect(true){
        if (way is Resource.Loading) {
            try {
                way = Resource.Success(api.getWay(id))
            } catch (e: Exception) {
                way = Resource.Error(e.message ?: "loading stations error")
            }
        }
    }
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                scope.launch { sheetState.collapse() }
            },
        topBar = {
            TopAppBar(title = (way as? Resource.Success)?.data?.name ?: "")
        },
        sheetContent = {
            Card(
                modifier = Modifier.padding(top = 4.dp),
                elevation = 0.dp,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                backgroundColor = Color.White
            ) {
                Column {
                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(3.dp)
                                .width(30.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.Gray)
                        )
                    }

            BottomSheetContent(wagon = pickWagon)
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
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
                            WagonButton(wagon = (wagon as Resource.Success<Wagon>).data, onClick = {pickWagon = ((wagon as Resource.Success<Wagon>).data); scope.launch { sheetState.expand() }})
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

@Composable
fun BottomSheetContent(wagon: Wagon?){
    Column(
        modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 10.dp)
    ) {
        if (wagon!=null) {
            Text("Вагон № ${wagon.inventoryNumber}")
            Text("Простой по станции ${wagon.idleDaysLength} дней")
            Text("Собственник № ${wagon.owner}")
        }
    }
}