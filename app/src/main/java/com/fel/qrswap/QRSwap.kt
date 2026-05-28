package com.fel.qrswap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fel.qrswap.data.CardViewModel
import com.fel.qrswap.data.CardViewModelFactory
import com.fel.qrswap.navigation.NavGraph


@Composable
fun QRSwap() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val app = context.applicationContext as QRSwapApplication

    val viewModel: CardViewModel = viewModel(
        factory = CardViewModelFactory(app.repository)
    )

    Surface {
        Scaffold(
            bottomBar = { BottomBar(navController) }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                NavGraph(navController, viewModel)
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "collection",
            onClick = { navController.navigate("collection") },
            label = { Text("Collection") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.playing_cards),
                    contentDescription = null
                )
            }
        )

        NavigationBarItem(
            selected = currentRoute == "create",
            onClick = { navController.navigate("create") },
            label = { Text("Create") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
            )}
        )

        NavigationBarItem(
            selected = currentRoute == "receive",
            onClick = { navController.navigate("receive") },
            label = { Text("Receive") },
            icon = {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = null
                )
            }
        )

        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") },
            label = { Text("Profile") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null
                )
            }
        )
    }
}