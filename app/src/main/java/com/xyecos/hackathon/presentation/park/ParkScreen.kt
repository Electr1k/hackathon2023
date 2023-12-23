package com.xyecos.hackathon.presentation.park

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xyecos.hackathon.data.Resource
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.Park
import com.xyecos.hackathon.data.dto.Way
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.common.TopAppBar
import com.xyecos.hackathon.presentation.stations.common.StationCard
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ParkScreen(
    api: ServerApi = ApiModule.provideApi(),
    id: Int,
    navigationToPark: (id: Int) -> Unit,
    popBack: () -> Unit
){
    var park: Resource<Park> by remember{
        mutableStateOf(Resource.Loading())
    }
    LaunchedEffect(true){
        if (park is Resource.Loading) {
            try {
                park = Resource.Success(api.getPark(id))
            } catch (e: Exception) {
                park = Resource.Error(e.message ?: "loading stations error")
            }
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = (park as? Resource.Success)?.data?.name ?: "")
        }

    ) { padding->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                ),
            contentPadding = PaddingValues(top = 14.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Пути",
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.W500)
            ) }
            when (park){
                is Resource.Success -> {
                    items((park as Resource.Success<Park>).data.waysIds) {
                        var way: Resource<Way> by remember{
                            mutableStateOf(Resource.Loading())
                        }
                        if (way is Resource.Loading){
                            LaunchedEffect(true){
                                try {
                                    way = Resource.Success(api.getWay(it))
                                } catch (e: Exception) {
                                    println(e)
                                }
                            }
                        }

                        if (way is Resource.Success) {
                            StationCard(
                                title = (way as Resource.Success<Way>).data.name,
                                onClick = {}
                            )
                        }
                        else{
                            StationCard(
                                title = null,
                                onClick = {}
                            )
                        }
                    }
                }
                is Resource.Loading -> {
                    println("Загрузка")
                    items(3) {
                        StationCard(
                            title = null,
                            onClick = {}
                        )
                    }
                }
                else -> {}
            }
        }
    }
}