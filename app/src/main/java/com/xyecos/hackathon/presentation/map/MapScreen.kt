package com.xyecos.hackathon.presentation.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.xyecos.hackathon.data.Resource
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.data.dto.StationById


@Composable
fun MapScreen(
    api: ServerApi,
    navigateToStationById: (StationById: Int) -> Unit
){
    val coordinatesA = LatLng(53.7381465, 87.0987051)
    val coordinatesB = LatLng(53.7637861, 87.5870778)
    val singapore = LatLng((coordinatesA.latitude + coordinatesB.latitude)/2, (coordinatesA.longitude + coordinatesB.longitude)/2)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 9f)
    }
    var stationsA: Resource<StationById> = Resource.Loading()
    var stationsB: Resource<StationById> = Resource.Loading()
    LaunchedEffect(true){
        try {
            stationsA = Resource.Success(api.getStation(1))
            stationsA = Resource.Success(api.getStation(2))
        }
        catch (e: Exception){
            stationsA = Resource.Error(e.message?:"")
            stationsB = Resource.Error(e.message?:"")
        }
    }


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,

    ){
        Marker(
            state = MarkerState(position = coordinatesA),
            title = "НОВОКУЗНЕЦК-СЕВЕРНЫЙ",
            snippet = "НОВОКУЗНЕЦК-СЕВЕРНЫЙ",
            onClick = {
                println(stationsA)
                //navigateToStationById(1);
                true}
        )

        Marker(
            state = MarkerState(position = coordinatesB),
            title = "Томусинская",
            snippet = "Томусинская",
            onClick = {
                println(stationsB)
                //navigateToStationById(2);
                true
            }
        )
    }

}