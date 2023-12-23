package com.xyecos.hackathon.presentation.navigation

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
    ),
    Park(
        route = "park/{$ID}",
    ),
    Way(
        route = "way/{$ID}"
    ),
    Map(
        route = "map"
    ),
    Login(
        route = "login"
    )
}