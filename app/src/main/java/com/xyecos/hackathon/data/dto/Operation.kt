package com.xyecos.hackathon.data.dto

data class Operation(
    val id: Int,
    val visibleNumber: String,
    val startDate: String,
    val endDate: String,
    val normalMinutesLength: Int,
    val operationId: Int,
    val operationName: String,
    val operationTypeId: Int,
    val reasonId: Int,
    val wagonsIds: List<Int>,
    val departureStation: Int,
    val departurePark: Int,
    val departureWay: Int,
    val destinationStation: Int,
    val destinationPark: Int,
    val destinationWay: Int,
    val supplyDirection: String,
    val locomotivesList: List<Int>,
    val operationStatus: String,
    val comment: String
)