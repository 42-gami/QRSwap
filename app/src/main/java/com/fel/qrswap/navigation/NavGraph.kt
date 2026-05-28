package com.fel.qrswap.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fel.qrswap.data.CardViewModel
import com.fel.qrswap.screens.CollectionScreen
import com.fel.qrswap.screens.CreateScreen
import com.fel.qrswap.screens.ProfileScreen
import com.fel.qrswap.screens.ReceiveScreen


sealed class Screen(val route: String) {
    object Collection: Screen("collection")
    object Create: Screen("create")
    object Receive: Screen("receive")
    object Profile: Screen("profile")
}

@Composable
fun NavGraph(navController: NavHostController, viewModel: CardViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.Collection.route
    ) {
        composable(Screen.Collection.route) { CollectionScreen(viewModel) }
        composable(Screen.Create.route) { CreateScreen(viewModel, navController) }
        composable(Screen.Receive.route) { ReceiveScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }
}