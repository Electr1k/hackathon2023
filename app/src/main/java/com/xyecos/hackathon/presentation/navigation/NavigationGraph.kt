package com.xyecos.hackathon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xyecos.hackathon.data.ServerApi
import com.xyecos.hackathon.di.ApiModule
import com.xyecos.hackathon.presentation.auth.MainLoginScreenContent
import com.xyecos.hackathon.presentation.park.ParkScreen
import com.xyecos.hackathon.presentation.detailed_station.DetailedStationScreen
import com.xyecos.hackathon.presentation.forms.MockFormsContent
import com.xyecos.hackathon.presentation.map.MapScreen
import com.xyecos.hackathon.presentation.splash.SplashScreen
import com.xyecos.hackathon.presentation.stations.StationsScreen
import com.xyecos.hackathon.presentation.way.WayScreen

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

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        route = ROOT_ROUTE
    ) {

        composable(route = Screen.Splash.route) {
            SplashScreen(openApp = {
                navigateByRoute(
                    route = Screen.Login.route,
                    popUpRoute = Screen.Splash.route,
                    isInclusive = true
                )
            })
        }

        composable(
            route = Screen.Stations.route
        ) {
            StationsScreen(
                navigateToStationById = { id, title ->
                    navigateByRoute(
                        Screen.DetailedStation.route.replace("{$ID}", id.toString())
                            .replace("{$TITLE}", title),
                        null
                    )
                },
                navigateToMap = { navigateByRoute(Screen.Map.route, null) },
                navigateToForms = { navigateByRoute(Screen.Forms.route, null) },
                popBack = { popBackStack() }
            )
        }

        composable(
            route = Screen.DetailedStation.route,
            arguments = listOf(
                navArgument(ID) { type = NavType.IntType },
                navArgument(TITLE) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            DetailedStationScreen(
                api = api,
                id = backStackEntry.arguments?.getInt(ID) ?: -1,
                navigationToPark = { id ->
                    navigateByRoute(
                        Screen.Park.route.replace("{$ID}", id.toString()),
                        null
                    )
                },
            )
        }

        composable(
            route = Screen.Park.route,
            arguments = listOf(
                navArgument(ID) { type = NavType.IntType },
            )
        ) { backStackEntry ->
            ParkScreen(
                id = backStackEntry.arguments?.getInt(ID) ?: -1,
                navigationToWay = { id ->
                    navigateByRoute(
                        Screen.Way.route.replace("{$ID}", id.toString()),
                        null
                    )
                },
            )
        }

        composable(
            route = Screen.Way.route,
            arguments = listOf(
                navArgument(ID) { type = NavType.IntType },
            )
        ) { backStackEntry ->
            WayScreen(
                id = backStackEntry.arguments?.getInt(ID) ?: -1,
            )
        }
        composable(route = Screen.Map.route) {
            MapScreen(
                api = api,
                navigateToStationById = { id ->
                    navigateByRoute(
                        Screen.DetailedStation.route.replace("{$ID}", id.toString()),
                        null
                    )
                })
        }

        composable(route = Screen.Forms.route) {
            MockFormsContent(
            )
        }

        composable(route = Screen.Login.route) {
            MainLoginScreenContent(navigationByRoute = { route -> navigateByRoute(route, null) })
        }
    }
}