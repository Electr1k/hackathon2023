package com.xyecos.hackathon.data

import com.xyecos.hackathon.data.dto.Operation

object MockRepo {
    val mockOperations = mutableListOf(
        Operation(
            id = 23121,
            visibleNumber = "23121",
            startDate = "2023-12-22 06:37:46",
            endDate = "2023-12-22 06:37:46",
            normalMinutesLength = 10,
            operationId = 8,
            operationName = "Подача вагонов",
            operationTypeId = 2,
            reasonId = 1,
            wagonsIds = listOf(1),
            departureStation = 1,
            departurePark = 10,
            departureWay = 1,
            destinationStation = 3,
            destinationPark = 5423,
            destinationWay = 232,
            supplyDirection = "LEFT",
            locomotivesList = listOf(1),
            operationStatus = "COMPLETED",
            comment = ""
        ),

        Operation(
            id = 3121,
            visibleNumber = "3121",
            startDate = "2023-12-24 06:37:46",
            endDate = "2023-12-24 06:37:46",
            normalMinutesLength = 10,
            operationId = 8,
            operationName = "Подача вагонов",
            operationTypeId = 2,
            reasonId = 6,
            wagonsIds = listOf(1),
            departureStation = 1,
            departurePark = 10,
            departureWay = 1,
            destinationStation = 3,
            destinationPark = 5423,
            destinationWay = 232,
            supplyDirection = "LEFT",
            locomotivesList = listOf(1),
            operationStatus = "OPEN",
            comment = ""
        )
    )
}