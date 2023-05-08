package com.begdev.bigbid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.begdev.bigbid.nav_utils.Screen
import com.begdev.bigbid.ui.auth.AuthenticationScreen
import com.begdev.bigbid.ui.main.MainScreen
import com.begdev.bigbid.ui.theme.BigBidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            BigBidTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Root.route){
                    rootGraph(navController)
                }
            }
        }
    }
}

fun NavGraphBuilder.rootGraph(navController: NavController) {
    navigation(startDestination = Screen.Auth.route, route = Screen.Root.route) {
        composable(Screen.Auth.route){
            AuthenticationScreen(navController)
        }
        composable(Screen.Main.route){
            MainScreen(navController)
        }
    }
}

//object RootNav {
//    const val ROOT_ROUTE = Screen.Root.route
//    const val AUTH_ROUTE = "auth"
//    const val MAIN_SCREEN = "main"
//}