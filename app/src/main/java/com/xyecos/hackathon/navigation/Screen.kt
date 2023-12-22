package com.xyecos.hackathon.navigation

enum class Screen(
    val route: String,
    val title: String? = null,
) {
    Stations(
        route = "stations",
        title = "Станции"
    ),
    DetailedStation(
        route = "stations/{$ID}/{$TITLE}",
    ),
    Splash(
        route = "splash"
    )
}