package com.begdev.bigbid.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.begdev.bigbid.nav_utils.Screen
import com.begdev.bigbid.ui.catalog.CatalogScreen
import com.begdev.bigbid.ui.favourite.FavouriteScreen
import com.begdev.bigbid.ui.item.ItemScreen
import com.begdev.bigbid.ui.profile.ProfileScreen
import com.begdev.bigbid.ui.theme.BigBidTheme


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
//    private val connectivityChecker: ConnectivityChecker

) {
    val isOnline by mainViewModel.isOnline.collectAsState()


    val navController = rememberNavController()
    val bottomBarItems = listOfNotNull(
        if (isOnline) Screen.Market else null,
        Screen.Favourite,
        Screen.Profile,
    )

        Surface(
//            modifier = Modifier
//                .fillMaxSize(),
        ) {
    BigBidTheme {
            Scaffold(
                topBar = {
                    if (!isOnline) TopAppBar(title = {
                        Text(
                            text = "OFFLINE MODE",
                            color = Color.Red
                        )
                    })
                },
                bottomBar = {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        bottomBarItems.forEach { screen ->
                            BottomNavigationItem(
                                icon = { Icon(screen.icon!!, contentDescription = null) },
                                label = { Text(stringResource(screen.resourceId!!)) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )

                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Favourite.route,
                    modifier = Modifier.padding(innerPadding)
                ) {

                    navigation(
                        route = Screen.Market.route,
                        startDestination = Screen.Catalog.route
                    ) {
                        composable(Screen.Catalog.route) {
                            CatalogScreen(navController)
                        }
                        composable(Screen.Item.route + "/{itemId}") {
                            ItemScreen()
                        }
                    }

                    composable(route = Screen.Favourite.route) {
                        FavouriteScreen()
                    }

                    composable(route = Screen.Profile.route) {
                        ProfileScreen()
                    }
                }
            }
        }
    }
}


