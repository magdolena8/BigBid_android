package com.begdev.bigbid.ui.main

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.begdev.bigbid.nav_utils.Destination
import com.begdev.bigbid.nav_utils.NavHost
import com.begdev.bigbid.nav_utils.NavigationIntent
import com.begdev.bigbid.nav_utils.Screen
import com.begdev.bigbid.nav_utils.composable
import com.begdev.bigbid.ui.auth.AuthenticationScreen
import com.begdev.bigbid.ui.home.HomeScreen
import com.begdev.bigbid.ui.item.ItemScreen
import com.begdev.bigbid.ui.profile.ProfileScreen
import com.begdev.bigbid.ui.theme.BigBidTheme
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Profile,
    )

    NavigationEffects(
        navigationChannel = mainViewModel.navigationChannel,
        navHostController = navController
    )
    BigBidTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsWithImePadding(),
        ) {
            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        items.forEach { screen ->
                            BottomNavigationItem(
                                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                                label = { Text(stringResource(screen.resourceId)) },
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
                    startDestination = Destination.HomeScreen,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(destination = Destination.LoginScreen) {
                        AuthenticationScreen()
                    }
//                    composable(destination = Destination.RegisterScreen) {
//                        RegisterScreen()
//                    }
                    composable(destination = Destination.HomeScreen) {
                        HomeScreen()
                    }
                    composable(destination = Destination.ItemScreen) {
                        ItemScreen()
                    }
                    composable(destination = Destination.ProfileScreen){
                        ProfileScreen()
                    }
//                composable(destination = Destination.UserDetailsScreen) {
//                    UserDetailsScreen()
//                }
                }
            }


//            NavHost(
//                navController = navController,
//                startDestination = Destination.HomeScreen
//            ) {
//                composable(destination = Destination.LoginScreen) {
//                    AuthenticationScreen()
//                }
//                composable(destination = Destination.RegisterScreen) {
//                    RegisterScreen()
//                }
//                composable(destination = Destination.HomeScreen) {
//                    HomeScreen()
//                }
//                composable(destination = Destination.ItemScreen) {
//                    ItemScreen()
//                }
////                composable(destination = Destination.UserDetailsScreen) {
////                    UserDetailsScreen()
////                }
//            }
        }
    }
}

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>, navHostController: NavHostController
) {
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity?.isFinishing == true) {
                return@collect
            }
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }

                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                        }
                    }
                }
            }
        }
    }
}
