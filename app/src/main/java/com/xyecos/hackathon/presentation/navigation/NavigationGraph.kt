package com.xyecos.hackathon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.detailed_station.DetailedStationScreen
import com.xyecos.hackathon.presentation.splash.SplashScreen
import com.xyecos.hackathon.presentation.stations.StationsScreen

@Composable
fun NavigationGraph(
    api: ServerApi = ApiModule.provideApi(),
    navController: NavHostController,
) {

    fun popBackStack() {
        navController.popBackStack()
    }


    fun navigateByRoute(
        route: String,
        popUpRoute: String? = null,
        isInclusive: Boolean = false,
        isSingleTop: Boolean = true,
    ) {
        navController.navigate(route) {
            popUpRoute?.let { popUpToRoute ->
                popUpTo(popUpToRoute) {
                    inclusive = isInclusive
                }
            }
            launchSingleTop = isSingleTop
        }
    }

    NavHost(navController = navController, startDestination = Screen.Splash.route, route = ROOT_ROUTE){

        composable(route = Screen.Splash.route){
            SplashScreen(openApp = {
                navigateByRoute(route = Screen.Stations.route,
                    popUpRoute = Screen.Splash.route,
                    isInclusive = true)
            })
        }

        composable(
            route = Screen.Stations.route
        ) {
            StationsScreen(
                api = api,
                navigateToStationById = { id, title ->
                    navigateByRoute(
                        Screen.DetailedStation.route.replace("{$ID}", id.toString()).replace("{$TITLE}", title),
                        null
                    )
                },
                popBack = { popBackStack() }
            )
        }

        composable(
            route = Screen.DetailedStation.route,
            arguments = listOf(
                navArgument(ID) { type = NavType.IntType },
                navArgument(TITLE) { type = NavType.StringType }
            )
        ){
            backStackEntry ->
            backStackEntry.arguments?.getInt(ID)
            DetailedStationScreen(
                api = api,
                id = backStackEntry.arguments?.getInt(ID) ?: -1,
                title = backStackEntry.arguments?.getString(TITLE) ?: "",
                popBack = { popBackStack() }
            )
        }
    }
}