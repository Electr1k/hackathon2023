package com.xyecos.hackathon.data

import com.xyecos.hackathon.data.dto.Station
import retrofit2.http.GET


interface ServerApi {

    @GET("stations")
    suspend fun getStations(): List<Station>

}