package com.begdev.bigbid.ui.app

import NavigationEffects
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.begdev.bigbid.nav_utils.Destination
import com.begdev.bigbid.nav_utils.NavHost
import com.begdev.bigbid.nav_utils.composable
import com.begdev.bigbid.ui.auth.AuthenticationScreen
import com.begdev.bigbid.ui.main.MainScreen
import com.begdev.bigbid.ui.theme.BigBidTheme
import com.google.accompanist.insets.navigationBarsWithImePadding

@Preview
@Composable
fun AppScreen(
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    NavigationEffects(
        navigationChannel = appViewModel.navigationChannel,
        navHostController = navController
    )
    BigBidTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsWithImePadding(),
        ) {
            NavHost(
                navController = navController,
                startDestination = Destination.MainScreen,
            ) {
                composable(destination = Destination.LoginScreen) {
                    AuthenticationScreen()
                }
                composable(destination = Destination.MainScreen) {
                    MainScreen()
                }

            }
        }
    }
}


//Scaffold(
//bottomBar = {
//    BottomNavigation {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentDestination = navBackStackEntry?.destination
//        items.forEach { screen ->
//            BottomNavigationItem(
//                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
//                label = { Text(stringResource(screen.resourceId)) },
//                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
//                onClick = {
//
//                    navController.navigate(screen.route) {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
//        }
//    }
//}
//) { innerPadding ->

