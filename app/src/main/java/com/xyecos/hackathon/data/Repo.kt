package com.xyecos.hackathon.data

import com.xyecos.hackathon.data.dto.Park
import com.xyecos.hackathon.data.dto.Station
import com.xyecos.hackathon.data.dto.StationById
import com.xyecos.hackathon.data.dto.Wagon
import com.xyecos.hackathon.data.dto.Way
import com.xyecos.hackathon.di.ApiModule
import kotlinx.coroutines.flow.MutableStateFlow

object Repo {
    val ready: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val api = ApiModule.provideApi()

    private val stationsFun = suspend { api.getStations() }

    private val fullStationsFun = suspend { api.getFullStations() }

    private val parksFun = suspend { api.getFullParks() }

    private val waysFun = suspend { api.getFullWays() }

    private val wagonsFun = suspend { getWays().flatMap { it.wagonsIds.map { wagonId -> api.getWagon(wagonId) } } }

    private lateinit var stations: List<Station>

    private lateinit var fullStations: List<StationById>

    private lateinit var parks: List<Park>

    private lateinit var ways: List<Way>

    private lateinit var wagons: List<Wagon>

    fun getStations() = stations

    fun getFullStations() = fullStations

    fun getParks() = parks

    fun getWays() = ways

    fun getWagons() = wagons

    suspend fun loadAll() {
        stations = stationsFun()
        fullStations = fullStationsFun()
        parks = parksFun()
        ways = waysFun()
        wagons = wagonsFun()

        ready.emit(true)
    }
}