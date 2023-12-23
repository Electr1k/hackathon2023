package com.xyecos.hackathon.data.dto

import com.xyecos.hackathon.presentation.way.wagons.Owner

data class Wagon(
    val wayId: Int,
    val parkId: Int,
    val stationId: Int,
    val id: Int,
    val inventoryNumber: String,
    val position: Int,
    val type: String,
    val isSick: Boolean,
    val cargo: String,
    val operationState: String,
    val idleDaysLength: Int,
    val owner: Owner,
    val isWithHatch: Boolean,
    val loadCapacity: Double,
    val daysToRepair: Int,
    val kilometersLeft: Int,
    val isDirty: Boolean,
    val note1: String,
    val note2: String
)
