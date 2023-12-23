package com.xyecos.hackathon.data.dto

data class StationById(
    val id: Int,
    val title: String,
    val isLoginStation:Boolean,
    val parksCount: Int,
    val parksIds: List<Int>
)
