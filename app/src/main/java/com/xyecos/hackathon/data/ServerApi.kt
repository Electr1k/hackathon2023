package com.xyecos.hackathon.data

import com.xyecos.hackathon.data.dto.Park
import com.xyecos.hackathon.data.dto.Station
import com.xyecos.hackathon.data.dto.StationById
import com.xyecos.hackathon.data.dto.Wagon
import com.xyecos.hackathon.data.dto.Way
import retrofit2.http.GET
import retrofit2.http.Path


interface ServerApi {

    @GET("stations")
    suspend fun getStations(): List<Station>

    @GET("stations/{id}")
    suspend fun getStation(
        @Path("id") id: Int
    ): StationById

    @GET("park/{id}")
    suspend fun getPark(
        @Path("id") id: Int
    ): Park

    @GET("way/{id}")
    suspend fun getWay(
        @Path("id") id: Int
    ): Way

    @GET("wagon/{id}")
    suspend fun getWagon(
        @Path("id") id: Int
    ): Wagon

    @GET("fullWagons")
    suspend fun getFullWagons(): List<Wagon>

    @GET("fullStations")
    suspend fun getFullStations(): List<StationById>

    @GET("fullParks")
    suspend fun getFullParks(): List<Park>

    @GET("fullWays")
    suspend fun getFullWays(): List<Way>

}